package edu.uiuc.cs.processmonitor.reciever;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.uiuc.cs.processmonitor.DataManager;

import android.app.ActivityManager;
import android.app.ActivityManager.RecentTaskInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.BatteryManager;
import android.os.Environment;
import android.util.Log;

public class BatteryMonitorReciever extends BroadcastReceiver {
	boolean monitoring = true;
	BatteryController controller = new BatteryController();
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("BatteryMonitorReciever", "Recieved broadcast, battery scanning!...");
		writeBatteryStuff(context, intent);
		if (monitoring) controller.onReceive(context, intent);
	}
	

	
	public static JSONObject writeBatteryStuff(Context context, Intent intent) {
		try {
			Log.d("BatteryMonitorReciever", "Recieved broadcast, battery scanning!...");
			
			int status_int = intent.getIntExtra("status", 0);
			String status_text;
			switch(status_int){
				case BatteryManager.BATTERY_STATUS_CHARGING:
					status_text = "Charging";
					break;
				case BatteryManager.BATTERY_STATUS_DISCHARGING:
					status_text = "Discharging";
					break;
				case BatteryManager.BATTERY_STATUS_FULL:
					status_text = "Full";
					break;
				case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
					status_text = "Not Charging";
					break;
				default:
					status_text = "Unknown";
					break;
			}
			
			int plugged_int = intent.getIntExtra("plugged", 0);
			String plugged_text;
			switch(plugged_int){
				case BatteryManager.BATTERY_PLUGGED_AC:
					plugged_text = "Plugged to AC";
					break;
				case BatteryManager.BATTERY_PLUGGED_USB:
					plugged_text = "Plugged to USB";
					break;
				default:
					plugged_text = "Not plugged";
					break;
			}
			
			int health_int = intent.getIntExtra("health", 0);
			String health_text;
			switch(health_int){
				case BatteryManager.BATTERY_HEALTH_DEAD:
					health_text = "Dead";
					break;
				case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
					health_text = "Over Voltage";
					break;
				case BatteryManager.BATTERY_HEALTH_GOOD:
					health_text = "Good";
					break;
				case BatteryManager.BATTERY_HEALTH_OVERHEAT:
					health_text = "Overheat";
					break;
				case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
					health_text = "Unspecified Failure";
					break;
				default:
					health_text = "Unknown";
					break;
			}
			

			Map<String,Object> mp = new HashMap<String, Object>();
			//mp.put("timestamp", new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
			mp.put("timestamp", System.currentTimeMillis());
			mp.put("status", status_text);
			mp.put("temperature", intent.getIntExtra("temperature", 0)/10);
			mp.put("level", intent.getIntExtra("level", 0));
			mp.put("scale", intent.getIntExtra("scale", 0));
			mp.put("plugged", plugged_text);
			mp.put("present", intent.getBooleanExtra("present", true));
			mp.put("technology", intent.getStringExtra("technology"));
			mp.put("voltage", intent.getIntExtra("voltage", 0) + " mV");
			mp.put("health", health_text);			
			
			JSONObject obj = new JSONObject(mp);
			return DataManager.saveBatteryEvent(context, obj);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
