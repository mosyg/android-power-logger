package edu.uiuc.cs.processmonitor.reciever;

import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import edu.uiuc.cs.processmonitor.DataManager;

public class BluetoothMonitor extends Monitor  {
	final static String TAG = "BluetoothMonitor";
	static BluetoothMonitor singleton;
	
	public static BluetoothMonitor getSingleton() {
		if (singleton == null)
			singleton = new BluetoothMonitor();
		return singleton;
	}
	
	public BluetoothMonitor() {
		super("Bluetooth", "bt");
	}

	public
	JSONObject scan(Context context, Intent intent) {
		try {		
			Log.d(TAG, "scanning bluetooth");
			JSONObject btinfo = new JSONObject();
			BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			if (mBluetoothAdapter == null) {
				Log.d(TAG, "No bluetooth. done");
				return null;
			}
			btinfo.put("enabled", mBluetoothAdapter.isEnabled());
			if (mBluetoothAdapter.isEnabled()) {
				Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
				JSONArray devices = new JSONArray();
				// If there are paired devices
				if (pairedDevices.size() > 0) {
				    // Loop through paired devices
				    for (BluetoothDevice device : pairedDevices) {
				    	JSONObject object = new JSONObject();
				        object.put("name", device.getName());
				        object.put("macaddr", device.getAddress());
				        object.put("bond_state", device.getBondState());
				    }
				}
			    btinfo.put("bonded", devices);
			}

			

			Log.d(TAG, "Writing to file: "+btinfo);
			return DataManager.saveBluetoothScan(context, btinfo);
			//Log.d(TAG, "complete");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
		
	
}
