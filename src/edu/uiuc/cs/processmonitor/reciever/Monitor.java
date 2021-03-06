package edu.uiuc.cs.processmonitor.reciever;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import edu.uiuc.cs.processmonitor.DataManager;

public class Monitor  {
	public final static String SHARED_PREFS = "monitorprefs";
	public final static String ENABLED = "_running";
	public final static String INTERVAL = "_interval";
	public final static String LAST_RAN = "_lastran";
	public String name;
	public String prefsPrefix; //the prefix to search for in preferences, eg "apps" for "apps_running" and "apps_lastran")
	
	public Monitor(String name, String prefsPrefix) {
		this.name = name;
		this.prefsPrefix = prefsPrefix;
	}
	
	
	public JSONObject scan(Context context, Intent intent) {
		//fill in yo stuff here
		return null;
	}
	
	
	public boolean isRunning(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS, 0);
		return prefs.getBoolean(prefsPrefix+ENABLED, false);
	}
	
	
	public void setRunning(Context context, boolean running) {        
		SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS, 0);
		Editor edit = prefs.edit();
    	edit.putBoolean(prefsPrefix+ENABLED, running);
        edit.commit();
	}
	
	public long getInterval(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS, 0);
		return prefs.getLong(prefsPrefix+INTERVAL, 30*1000);
	}
	
	public void setInterval(Context context, long value) {
		SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS, 0);
		Editor edit = prefs.edit();
		edit.putLong(prefsPrefix+INTERVAL, value);
		edit.commit();
	}
}
