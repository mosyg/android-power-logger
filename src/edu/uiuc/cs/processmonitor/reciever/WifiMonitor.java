package edu.uiuc.cs.processmonitor.reciever;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import edu.uiuc.cs.processmonitor.DataManager;

public class WifiMonitor extends Monitor  {
	private final static boolean DEBUG = false;
	static WifiMonitor singleton;
	
	public static WifiMonitor getSingleton() {
		if (singleton == null)
			singleton = new WifiMonitor();
		return singleton;
	}
	
	public WifiMonitor() {
		super("Wifi", "wifi");
	}

	public
	JSONObject scan(Context context, Intent intent) {
		try {		
			Log.d("WifiMonitor", "scanning wifi");
			JSONObject wifiinfo = new JSONObject();
			WifiManager manager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
			if (manager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
				WifiInfo current = manager.getConnectionInfo();
				wifiinfo.put("netid", current.getNetworkId());
				wifiinfo.put("macaddr", current.getBSSID());
				wifiinfo.put("ssid", current.getSSID());
				wifiinfo.put("rssi", current.getRssi());
				JSONArray wifiscan = new JSONArray();
				for (ScanResult scan: manager.getScanResults()) {
					JSONObject scanitem = new JSONObject();
					scanitem.put("macaddr", scan.BSSID);
					scanitem.put("rssi", scan.level);
					scanitem.put("ssid", scan.SSID);
					wifiscan.put(scanitem);
				}
				wifiinfo.put("scan", wifiscan);
			}
			wifiinfo.put("state", manager.getWifiState());
			

			if (DEBUG) Log.d("WifiMonitor", "Writing to file: "+wifiinfo.toString(4));
			return DataManager.saveWifiScan(context, wifiinfo);
			//Log.d("WifiMonitor", "complete");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
		
	
}
