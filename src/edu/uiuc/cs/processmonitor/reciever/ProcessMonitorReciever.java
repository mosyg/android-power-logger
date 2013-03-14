package edu.uiuc.cs.processmonitor.reciever;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.util.Log;
import edu.uiuc.cs.processmonitor.DataManager;

public class ProcessMonitorReciever extends BroadcastReceiver {
	public final static String PROCESS_SHARED_PREFS = ProcessMonitorReciever.class.getPackage().toString() + ".PROCESS_SHARED_PREFS";
	public final static String LASTSCANNED_SHARED_PREFS = ProcessMonitorReciever.class.getPackage().toString() + ".LASTSCANNED_SHARED_PREFS";
	public final static String TIME_OF_LAST_SCAN = ProcessMonitorReciever.class.getPackage().toString() + ".LAST_SCAN";
	public final static String INTERVAL = ProcessMonitorReciever.class.getPackage().toString() + ".INTERVAL";
	public final static String RUNNING = ProcessMonitorReciever.class.getPackage().toString() + ".RUNNING";
	public final static String ENABLED = ProcessMonitorReciever.class.getPackage().toString() + ".ENABLED";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			JSONArray collection = new JSONArray();
			long start = System.currentTimeMillis();
			Log.d("ProcessMonitorReciever", "Waking up and starting scan...");
			if (ProcessMonitor.getSingleton().isRunning(context)) {
				Log.d("ProcessMonitorReciever", "scanning running apps");
				collection.put(ProcessMonitor.getSingleton().scan(context, intent));
			}
			if (ProcessesMonitor.getSingleton().isRunning(context)) {
				Log.d("ProcessMonitorReciever", "scanning processes");
				collection.put(ProcessesMonitor.getSingleton().scan(context, intent));
			}
			if (AppEventsMonitor.getSingleton().isRunning(context)) {
				Log.d("ProcessMonitorReciever", "scanning app events? not sure how this works.");
				collection.put(AppEventsMonitor.getSingleton().scan(context, intent));
			}
			if (WifiMonitor.getSingleton().isRunning(context)) {
				Log.d("ProcessMonitorReciever", "scanning wifi");
				collection.put(WifiMonitor.getSingleton().scan(context, intent));
			}
			if (BluetoothMonitor.getSingleton().isRunning(context)) {
				Log.d("ProcessMonitorReciever", "scanning bluetooth");
				collection.put(BluetoothMonitor.getSingleton().scan(context, intent));
			}
			if (1==1 || GPSMonitor.getSingleton().isRunning(context)) { //hack. testing GPS now.
				Log.d("ProcessMonitorReciever", "scanning GPS");
				collection.put(GPSMonitor.getSingleton().scan(context, intent));
			}
			Log.d("ProcessMonitorReciever", "Logging timestamp");
			saveTimestamp(context);
			
			long timeEnd = System.currentTimeMillis();
			Log.d("ProcessMonitorReciever", "Scan complete. total "+(timeEnd-start)+" milliseconds. sleeping");
			sendList(collection, context);
		} catch (Exception e) {
			e.printStackTrace();

		}
	}
	
	private void sendList(JSONArray collection, Context context) {
		try {
			JSONObject sendobject = new JSONObject();
			sendobject.put(DataManager.KIND, "multipart");
			sendobject.put("collection", collection);
			TelephonyManager tman = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			if (tman.getDeviceId() != null)
				sendobject.put("IMEI", tman.getDeviceId());
			else 
				sendobject.put("IMEI", Build.BOARD+Build.BOOTLOADER+Build.DEVICE+Build.FINGERPRINT+Build.HARDWARE+Build.HOST+Build.SERIAL+Build.HARDWARE);
			//DataManager.writeToUrl(DataManager.URL, sendobject);
			//DataManager.writeToUrlUnthreaded(DataManager.URL, sendobject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	private void saveTimestamp(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(LASTSCANNED_SHARED_PREFS, 0);
		Editor edit = prefs.edit();
		edit.putLong(TIME_OF_LAST_SCAN, System.currentTimeMillis());
		edit.commit(); 
	}
	
	public static long getLastScan(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(LASTSCANNED_SHARED_PREFS, 0);
		return prefs.getLong(TIME_OF_LAST_SCAN, -1);
	}

	
	
	
	public static void checkAndStartStop(Context context) {
		startStopSelf(context, isEnabled(context));
	}
	
	
	public static void startStopSelf(Context context, boolean start) {
		long repeatTime = getInterval(context);
        AlarmManager mgr=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i=new Intent(context, ProcessMonitorReciever.class);
        PendingIntent pi=PendingIntent.getBroadcast(context, 0, i, 0);

    	mgr.cancel(pi);

        if (start)
        	mgr.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), repeatTime, pi);
        
		setRunning(context, start);
	}

	
	public static boolean isRunning(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(PROCESS_SHARED_PREFS, 0);
		return prefs.getBoolean(RUNNING, false);
	}
	
	
	public static void setRunning(Context context, boolean running) {        
		SharedPreferences prefs = context.getSharedPreferences(PROCESS_SHARED_PREFS, 0);
		Editor edit = prefs.edit();
    	edit.putBoolean(RUNNING, running);
        edit.commit();
	}
	
	public static boolean isEnabled(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(PROCESS_SHARED_PREFS, 0);
		return prefs.getBoolean(ENABLED, false);
	}
	
	
	public static void setEnabled(Context context, boolean running) {        
		SharedPreferences prefs = context.getSharedPreferences(PROCESS_SHARED_PREFS, 0);
		Editor edit = prefs.edit();
    	edit.putBoolean(ENABLED, running);
        edit.commit();
	}


	
	
	public static long getInterval(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(PROCESS_SHARED_PREFS, 0);
		return prefs.getLong(INTERVAL, 30*1000);
	}
	

	
	public static void setInterval(Context context, long value) {
		SharedPreferences prefs = context.getSharedPreferences(PROCESS_SHARED_PREFS, 0);
		Editor edit = prefs.edit();
		edit.putLong(INTERVAL, value);
		edit.commit();
	}
	

}
