package edu.uiuc.cs.processmonitor;

import edu.uiuc.cs.processmonitor.reciever.ProcessMonitorReciever;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

public class ProcessMonitorActivity extends Activity {
	Button startbutton;
	Button stopbutton;
	TextView lastscan;
	public long REPEAT_TIME = 1000 * 30;//time we repeat the scan
	
	public long updateuitime = 1000 * 1;
	boolean keepupdating = true;
	Handler updateHandler;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        lastscan = (TextView)findViewById(R.id.titletext);
        startbutton = (Button)findViewById(R.id.starttbutton);
        startbutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
		        AlarmManager mgr=(AlarmManager)ProcessMonitorActivity.this.getSystemService(Context.ALARM_SERVICE);
		        Intent i=new Intent(ProcessMonitorActivity.this, ProcessMonitorReciever.class);
		        PendingIntent pi=PendingIntent.getBroadcast(ProcessMonitorActivity.this, 0, i, 0);

		        mgr.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), REPEAT_TIME, pi);        
			}
		});
        stopbutton = (Button)findViewById(R.id.stoppbutton);
        stopbutton.setOnClickListener(new OnClickListener() {
 			@Override
 			public void onClick(View v) {
 		        AlarmManager mgr=(AlarmManager)ProcessMonitorActivity.this.getSystemService(Context.ALARM_SERVICE);
 		        Intent i=new Intent(ProcessMonitorActivity.this, ProcessMonitorReciever.class);
 		        PendingIntent pi=PendingIntent.getBroadcast(ProcessMonitorActivity.this, 0, i, 0);

 		        mgr.cancel(pi);
 			}
 		});
        
        
        ((Button)findViewById(R.id.vizbutton)).setOnClickListener(new OnClickListener() {
 			@Override
 			public void onClick(View v) {
 				Intent intent = new Intent(ProcessMonitorActivity.this, AppVizActivity.class);
 				startActivity(intent);
 			}
 		});

//        doBindService();
        updateHandler = new Handler();
        
        
        
    }
    
    Runnable updateLastScan = new Runnable() {
    	public void run() {
    		if (lastscan != null)  {
	    		String timeinpast = "";
	    		SharedPreferences prefs = ProcessMonitorActivity.this.getSharedPreferences(ProcessMonitorReciever.PROCESS_SHARED_PREFS, 0);
	    		long lastscantime = prefs.getLong(ProcessMonitorReciever.TIME_OF_LAST_SCAN, -1);
	    		if (lastscantime < 0)
	    			lastscan.setText("No previous scans");
	    		long diff = System.currentTimeMillis() - lastscantime;
	    		if (diff < 1000 * 60) 
	    			timeinpast = (diff / 1000) + " seconds ago";
	    		else if (diff < 1000 * 60 * 60)
	    			timeinpast = (diff / 1000 / 60) + " minutes and " + ( (diff / 1000)%60 ) + " seconds ago";
	    		else 
	    			timeinpast = (diff / 1000 / 60 / 60) + " hours and " + ( (diff / 1000 / 60 )%60 ) + " minutes ago";
	    		lastscan.setText("Last scan: "+timeinpast);
    		}
    		runagainmaybe(true);
    	}
    };
    
    public void runagainmaybe( boolean infuture) {
    	if (infuture) {
    		if (keepupdating) updateHandler.postAtTime(updateLastScan, SystemClock.uptimeMillis() + updateuitime);
    	} else {
    		updateHandler.post(updateLastScan);
    	}
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    	keepupdating = false;
    }
    
    @Override
    public void onStart() {
    	super.onStart();
    	keepupdating = true;
    	runagainmaybe(false);
    }

}