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

public class BootupReciever extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		ProcessMonitorReciever.checkAndStartStop(context);
	}

}
