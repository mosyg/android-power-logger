package edu.uiuc.cs.processmonitor.reciever;

import org.json.JSONObject;

import edu.uiuc.cs.processmonitor.DataManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class AppEventReciever extends BroadcastReceiver  {

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			Bundle b = intent.getExtras();				
			Log.d("AppEventRecieverrrrrrr", "intent extras: "+b);
			Log.d("AppEventRecieverrrrrrr", "intent: "+intent.getAction());
	
//			String action = intent.getAction();
//			if (action.equals("com.android.alarmclock.ALARM_ALERT") || 
//					action.equals("com.htc.android.worldclock.ALARM_ALERT") || 
//					action.equals("com.android.deskclock.ALARM_ALERT") || 
//					action.equals("com.sonyericsson.alarm.ALARM_ALERT") ) {
//			}
			
			
			Bundle bundle = intent.getExtras();				
			JSONObject object = new JSONObject();
			object.put("action", intent.getAction());
			object.put("intent", intent.toString());
			if (bundle != null) {
				JSONObject jsbundle = new JSONObject();
				for (String key : bundle.keySet()) {
					jsbundle.put(key, bundle.get(key));
				}
				object.put("extras", jsbundle);
			}
//			Log.d("AppEventReciever", "Alarm intent: "+bundle);
			DataManager.saveSoftEvent(context, object);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
