package edu.uiuc.cs.processmonitor.reciever;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import edu.uiuc.cs.processmonitor.DataManager;

public class ProcessesMonitor extends Monitor  {
	final static String TAG = "ProcessesMonitor";
	static ProcessesMonitor singleton;
	
	public static ProcessesMonitor getSingleton() {
		if (singleton == null)
			singleton = new ProcessesMonitor();
		return singleton;
	}
	
	public ProcessesMonitor() {
		super("Processes", "proc");
	}

	public
	JSONObject scan(Context context, Intent intent) {
		try {		
			Log.d(TAG, "scanning processes");
//			JSONObject btinfo = new JSONObject();
//			
//
//			Log.d(TAG, "Writing to file: "+btinfo);
//			DataManager.saveBluetoothScan(context, btinfo);
			JSONObject scans = new JSONObject();
			
			Long timeStart = System.currentTimeMillis();
			scans.put("kind", "ProcessesMonitorScan");
			scans.put("nonJVM", nonJVMprocessesWrite(context));
			ActivityManager a = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			scans.put("runningAppProcesses", runningAppsWrite(context,  a.getRunningAppProcesses()));
//			runningServicesWrite(context, a.getRunningServices(50));
//			runningTasksWrite(context, a.getRunningTasks(50));
			scans.put("meminfo", DataManager.processes_memInfoWrite(context));
			
			Long timeEnd = System.currentTimeMillis();
			
			Log.d(TAG, "complete. total time: "+(timeEnd-timeStart)+" milliseconds");
			return scans;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	static public JSONObject nonJVMprocessesWrite(Context context){
	    
    	try {
    	    // Executes the command.
    		Process process;
    		if (new File("/system/xbin/ps").exists()) {
	    	    process = Runtime.getRuntime().exec("/system/xbin/ps");
    		} else  {//if (new File("/system/bin/ps/").exists()) {
    			process = Runtime.getRuntime().exec("/system/bin/ps");
    		} 
    	    BufferedReader reader = new BufferedReader(
    	            new InputStreamReader(process.getInputStream()));
    		JSONArray array = new JSONArray();

    	    String line = reader.readLine();
    	    //Log.e("***** lineBOOOO", "" + line);
    	    long system_time = System.currentTimeMillis();
    	    
    	    while((line = reader.readLine()) != null){
	    	    try {
	    	        int i = 0;
	        	    //Log.e("***** line",  line); 
	    	        while(line.charAt(i) < '0' || line.charAt(i) > '9') i++;
	    	        i++;
	    	        int j = i;
	    	        while(line.charAt(j) >= '0' && line.charAt(j) <= '9') j++;
	    	        //Log.e("***** i, j",  "*" + (i-1) + "*" + "*" + j + "*");
	    	        String pid = line.substring(i-1, j); 
	        	    //Log.e("***** pid",  "*" + pid + "*"); 
	        	    j++;
	        	    i = j;
	        	    while(line.charAt(j) >= '!' && line.charAt(j) <= '~') j++;
	    	        //Log.e("***** i, j",  "*" + (i-1) + "*" + "*" + j + "*");
	    	        String user = line.substring(i, j); 
	        	    //Log.e("***** user",  "*" + user + "*");         	    
	        	    j++;
	        	    i = j;        	    
	        	    while(line.charAt(i) < '0' || line.charAt(i) > '9') i++;
	    	        i++;
	    	        j = i;
	    	        while(line.charAt(j) >= '0' && line.charAt(j) <= '~') j++;
	    	        String vsz = line.substring(i-1, j);
	        	    //Log.e("***** vsz",  "*" + vsz + "*");        	    
	        	    j++;
	        	    while(line.charAt(j) < 'A' || line.charAt(j) > 'Z') j++;
	        	    j++;
	        	    i = j;
	        	    while(line.charAt(j) >= 'A' && line.charAt(j) <= 'Z') j++;
	        	    String stat = line.substring(i-1, j);
	        	    //Log.e("***** stat",  "*" + stat + "*");
	        	    
	        	    j++;
	        	    while(line.charAt(j) < '!' || line.charAt(j) > '~') j++;
	        	    String command = line.substring(j, line.length());
	        	    //Log.e("***** command",  "*" + command + "*");        	    
	        	    
	        	    Map<String,Object> mp = new HashMap<String, Object>();
	    			mp.put("timestamp", system_time);
	    			mp.put("pid", pid);
	    			mp.put("user", user);
	    			mp.put("vsz", vsz);
	    			mp.put("stat", stat);
	    			mp.put("command", command);
	    			array.put(new JSONObject(mp));
    	    	} catch (Exception e) {
    	    		//who cares.
    	    	}

    	    }
    		JSONObject obj = new JSONObject();
    		try { obj.put("data", array); } catch (Exception e) {e.printStackTrace();} 
    	    JSONObject obj2 = DataManager.processes_write("nonJVMprocesses.json", obj);

    	    
    	    reader.close();    	    
    	    process.waitFor();
    	    return obj2;
    	    
    	} catch (IOException e) {
    	    throw new RuntimeException(e);
    	} catch (InterruptedException e) {
    	    throw new RuntimeException(e);
    	}
	}
	
	static public JSONObject runningAppsWrite(Context context, List<RunningAppProcessInfo> runningApps){
		
		long system_time = System.currentTimeMillis();
		JSONArray array = new JSONArray();
		for(int i = 0; i < runningApps.size(); i++){
			RunningAppProcessInfo r = runningApps.get(i);
			
			ActivityManager a = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	        int[] boo = {r.pid};
	        android.os.Debug.MemoryInfo[] m = a.getProcessMemoryInfo(boo);			
			
			Map<String,Object> mp = new HashMap<String, Object>();
			mp.put("timestamp", system_time);
			mp.put("importance", r.importance);
			mp.put("importance_reason_code", r.importanceReasonCode);
			mp.put("importance_reason_component", r.importanceReasonComponent);
			mp.put("importance_reason_pid", r.importanceReasonPid);
			mp.put("lru", r.lru);
			mp.put("pid", r.pid);
			mp.put("package_list", r.pkgList);
			mp.put("process_name", r.processName);
			mp.put("user_id", r.uid);
			
			mp.put("Dalvik_private_dirty_pages", m[0].dalvikPrivateDirty);
			mp.put("Dalvik_proportional_set_size", m[0].dalvikPss);
			mp.put("Dalvik_shared_dirty_pages", m[0].dalvikSharedDirty);
			mp.put("Native_private_dirty_pages", m[0].nativePrivateDirty);
			mp.put("Native_proportional_set_size", m[0].nativePss);
			mp.put("Native_shared_dirty_pages", m[0].nativeSharedDirty);
			mp.put("Other_private_dirty_pages", m[0].otherPrivateDirty);
			mp.put("Other_proportional_set_size", m[0].otherPss);
			mp.put("Other_shared_dirty_pages", m[0].otherSharedDirty);
		
			array.put(new JSONObject(mp));
		}
		JSONObject obj = new JSONObject();
		try { obj.put("data", array); } catch (Exception e) {e.printStackTrace();} 
		return DataManager.processes_write("jvmrunningApps.json", obj);
	}
	
	static public void runningServicesWrite(Context context, List<RunningServiceInfo> runningServices){

		long system_time = System.currentTimeMillis();
		JSONArray array = new JSONArray();
		for(int i = 0; i < runningServices.size(); i++){
			RunningServiceInfo r = runningServices.get(i);
			
			Map<String,Object> mp = new HashMap<String, Object>();
			mp.put("timestamp", system_time);
			mp.put("active_since", r.activeSince);
			mp.put("client_count", r.clientCount);
			mp.put("client_label", r.clientLabel);
			mp.put("client_package", r.clientPackage);
			mp.put("crash_count", r.crashCount);
			mp.put("flags", r.flags);
			mp.put("foreground", r.foreground);
			mp.put("last_activity_time", r.lastActivityTime);
			mp.put("pid", r.pid);
			mp.put("process", r.process);
			mp.put("restarting", r.restarting);
			mp.put("service", r.service);
			mp.put("started", r.started);
			mp.put("uid", r.uid);
			
			array.put(new JSONObject(mp));
			
		}
		JSONObject obj = new JSONObject();
		try { obj.put("data", array); } catch (Exception e) {e.printStackTrace();} 
		DataManager.processes_write("runningServices.json", obj);
	}
	
	static public void runningTasksWrite(Context context, List<RunningTaskInfo> runningTasks){

		long system_time = System.currentTimeMillis();
		
		JSONArray tasks = new JSONArray();
		
		for(int i = 0; i < runningTasks.size(); i++){
			RunningTaskInfo r = runningTasks.get(i);
			
			Map<String,Object> mp = new HashMap<String, Object>();
			mp.put("timestamp", system_time);
			mp.put("base_activity", r.baseActivity);
			mp.put("description", r.description);
			mp.put("id", r.id);
			mp.put("num_activities", r.numActivities);
			mp.put("num_running", r.numRunning);
			mp.put("top_activity", r.topActivity);
			
			tasks.put(new JSONObject(mp));
		}
		JSONObject obj = new JSONObject();
		try { obj.put("data", tasks); } catch (Exception e) {e.printStackTrace();} 
		DataManager.processes_write("runningTasks.json", obj);

	}
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
