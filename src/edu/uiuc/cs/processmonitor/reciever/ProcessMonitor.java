package edu.uiuc.cs.processmonitor.reciever;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.ActivityManager.RecentTaskInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import edu.uiuc.cs.processmonitor.DataManager;

public class ProcessMonitor  extends Monitor{
	private final static boolean DEBUG = false;
	static ProcessMonitor singleton;
	
	public static ProcessMonitor getSingleton() {
		if (singleton == null)
			singleton = new ProcessMonitor();
		return singleton;
	}
	
	public ProcessMonitor() {
		super("Running Apps", "apps");
	}

	
	
	public JSONObject scan(Context context, Intent intent) {
		try {			
			ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
			List<RecentTaskInfo> recentTasks  = manager.getRecentTasks(50, ActivityManager.RECENT_WITH_EXCLUDED);
			JSONArray jsontasks = new JSONArray();
			for (RecentTaskInfo task : recentTasks) {
				//jsontasks.put(task.)
				//Log.d("ProcessMonitorReciever", "Task: id:"+task.id+" baseIntent"+task.baseIntent+" origActivity:"+task.origActivity+" yeah");
				//Log.d("ProcessMonitorReciever", "Task: id:"+task.id + " activity:" + task.baseIntent.getComponent());
				JSONObject taskobj = new JSONObject();
				taskobj.put("id", task.id);
				taskobj.put("package", task.baseIntent.getComponent().getPackageName());
				if (task.origActivity != null) taskobj.put("orig", task.origActivity.getPackageName());
				jsontasks.put(taskobj);
			}
			
			JSONArray jsonforeground = new JSONArray();
		    ActivityManager mActivityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		    List <RunningAppProcessInfo> l = mActivityManager.getRunningAppProcesses();
		    List <RunningServiceInfo> service = mActivityManager.getRunningServices(9999);
		    HashMap<String, Boolean> runningServices = new HashMap<String,Boolean>();
		    for (RunningServiceInfo info : service) runningServices.put(info.process, true);
		    for (RunningAppProcessInfo info : l) {
		    	JSONObject fgobj = new JSONObject();
		    	fgobj.put("process", info.processName);
		    	switch (info.importance) {
			    	case RunningAppProcessInfo.IMPORTANCE_FOREGROUND:
			    	fgobj.put("importance", "foreground");
			    	break;
			    	case RunningAppProcessInfo.IMPORTANCE_BACKGROUND:
			    	fgobj.put("importance", "background");
			    	break;
			    	case RunningAppProcessInfo.IMPORTANCE_SERVICE:
			    	fgobj.put("importance", "service");
			    	break;
			    	case RunningAppProcessInfo.IMPORTANCE_VISIBLE:
			    	fgobj.put("importance", "visible");
			    	break;
			    	default:
			    		break;
		    	}
		    	if (runningServices.containsKey(info.processName)) fgobj.put("service", runningServices.get(info.processName).booleanValue());
		    	else fgobj.put("service", false);
		    	JSONArray pcklist = new JSONArray();
		    	for (String pckg : info.pkgList) pcklist.put(pckg);
		    	fgobj.put("packagelist", pcklist);
		    	jsonforeground.put(fgobj);
		    }

			
			if (DEBUG) Log.d("ProcessMonitorReciever", "Writing to file:"+jsontasks.toString(4));
			if (DEBUG) Log.d("ProcessMonitorReciever", "Writing to file:"+jsonforeground.toString(4));
			return DataManager.saveScan(context, jsontasks, jsonforeground);
			//Log.d("ProcessMonitorReciever", "Scan complete. sleeping");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
		
	
	
	
	private static ArrayList<RunningAppProcessInfo> getForegroundApps(Context mContext) {
	    ArrayList<RunningAppProcessInfo> array = new ArrayList<RunningAppProcessInfo>();

	    ActivityManager mActivityManager = (ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE);
	    List <RunningAppProcessInfo> l = mActivityManager.getRunningAppProcesses();
	    for (RunningAppProcessInfo info : l) {
	    	if (info.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
	    		array.add(info);
	    	}
	    }
	    return array;
	}
	
	
	
	
	
	
	
	
	private static RunningAppProcessInfo getForegroundApp(Context context) {
	    RunningAppProcessInfo result=null, info=null;

	    ActivityManager    mActivityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
	    List <RunningAppProcessInfo> l = mActivityManager.getRunningAppProcesses();
	    Iterator <RunningAppProcessInfo> i = l.iterator();
	    while(i.hasNext()){
	        info = i.next();
	        if(info.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND
	                && !isRunningService(info.processName, mActivityManager, context)){
	            result=info;
	            break;
	        }
	    }
	    return result;
	}
	
	private static ComponentName getActivityForApp(RunningAppProcessInfo target, Context mContext){
	    ComponentName result=null;
	    ActivityManager.RunningTaskInfo info;

	    if(target==null)
	        return null;

	    ActivityManager   mActivityManager = (ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE);
	    List <ActivityManager.RunningTaskInfo> l = mActivityManager.getRunningTasks(9999);
	    Iterator <ActivityManager.RunningTaskInfo> i = l.iterator();

	    while(i.hasNext()){
	        info=i.next();
	        if(info.baseActivity.getPackageName().equals(target.processName)){
	            result=info.topActivity;
	            break;
	        }
	    }

	    return result;
	}

	private static boolean isStillActive(RunningAppProcessInfo process, ComponentName activity, Context mContext)
	{
	    // activity can be null in cases, where one app starts another. for example, astro
	    // starting rock player when a move file was clicked. we dont have an activity then,
	    // but the package exits as soon as back is hit. so we can ignore the activity
	    // in this case
	    if(process==null)
	        return false;

	    RunningAppProcessInfo currentFg=getForegroundApp(mContext);
	    ComponentName currentActivity=getActivityForApp(currentFg, mContext);

	    if(currentFg!=null && currentFg.processName.equals(process.processName) &&
	            (activity==null || currentActivity.compareTo(activity)==0))
	        return true;

	    Log.i("ProcessMonitor", "isStillActive returns false - CallerProcess: " + process.processName + " CurrentProcess: "
	            + (currentFg==null ? "null" : currentFg.processName) + " CallerActivity:" + (activity==null ? "null" : activity.toString())
	            + " CurrentActivity: " + (currentActivity==null ? "null" : currentActivity.toString()));
	    return false;
	}

	private static boolean isRunningService(String processname, ActivityManager mActivityManager, Context mContext){
	    if(processname==null || processname.equals(""))
	        return false;

	    RunningServiceInfo service;

	    if(mActivityManager==null)
	        mActivityManager = (ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE);
	    List <RunningServiceInfo> l = mActivityManager.getRunningServices(9999);
	    Iterator <RunningServiceInfo> i = l.iterator();
	    while(i.hasNext()){
	        service = i.next();
	        if(service.process.equals(processname))
	            return true;
	    }

	    return false;
	}


}
