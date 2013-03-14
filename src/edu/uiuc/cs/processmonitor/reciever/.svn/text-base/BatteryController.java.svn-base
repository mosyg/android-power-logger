package edu.uiuc.cs.processmonitor.reciever;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import android.content.Context;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.BatteryManager;
import android.util.Log;

public class BatteryController  {
	
	int high = 70;
	int low = 30;
	public void onReceive(Context context, Intent intent) {
		int status_int = intent.getIntExtra("status", 0);
		int level = intent.getIntExtra("level", 0);
		Log.d("BatteryController", "New level: status:"+status_int+" level:"+level);
		if (status_int == BatteryManager.BATTERY_STATUS_CHARGING) {
			if (level >= high) {
				fireCharged(context, intent);
			}
		}
		else if (status_int == BatteryManager.BATTERY_STATUS_DISCHARGING) {
			if (level <= low) {
				fireCharged(context, intent);
			}
		}
		

	}
	

	
	public void fireCharged(Context context, Intent intent) {
		ProcessMonitorReciever.setEnabled(context, true);
		ProcessMonitorReciever.checkAndStartStop(context);
		
		try {
			//HttpGet get = new HttpGet("http://someurl.com");
			//HttpClient client = AndroidHttpClient.newInstance("asdf");
			//client.execute(get);
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void fireDischarged(Context context, Intent intent) {
		try {
			//HttpGet get = new HttpGet("http://someurl.com");
			//HttpClient client = AndroidHttpClient.newInstance("asdf");
			//client.execute(get);
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
