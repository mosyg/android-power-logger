package edu.uiuc.cs.processmonitor.reciever;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class AppEventsMonitor extends Monitor  {
	final static String TAG = "AppEventsMonitor";
	static AppEventsMonitor singleton;
	
	public static AppEventsMonitor getSingleton() {
		if (singleton == null)
			singleton = new AppEventsMonitor();
		return singleton;
	}
	
	public AppEventsMonitor() {
		super("App Events", "appevents");
	}

	public
	JSONObject scan(Context context, Intent intent) {
		try {		
//			Log.d(TAG, "scanning bluetooth");
//			JSONObject btinfo = new JSONObject();
//			
//
//			Log.d(TAG, "Writing to file: "+btinfo);
//			DataManager.saveBluetoothScan(context, btinfo);
//			Log.d(TAG, "complete");
			Intent i = context.getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
			return BatteryMonitorReciever.writeBatteryStuff(context, i);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
		
	
}
