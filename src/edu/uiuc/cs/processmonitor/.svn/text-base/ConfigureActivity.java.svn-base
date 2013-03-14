package edu.uiuc.cs.processmonitor;

import android.app.Activity;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import edu.uiuc.cs.processmonitor.reciever.AppEventsMonitor;
import edu.uiuc.cs.processmonitor.reciever.BluetoothMonitor;
import edu.uiuc.cs.processmonitor.reciever.ProcessMonitor;
import edu.uiuc.cs.processmonitor.reciever.ProcessMonitorReciever;
import edu.uiuc.cs.processmonitor.reciever.ProcessesMonitor;
import edu.uiuc.cs.processmonitor.reciever.WifiMonitor;

public class ConfigureActivity extends Activity {
	CheckBox globalenabled;
	CheckBox allapps;
	CheckBox runningapps;
	CheckBox processes;
	CheckBox softevents;
	CheckBox location;
	CheckBox wifi;
	CheckBox bluetooth;
	
	TextView lastScan;
	boolean keepupdating = true;
	Handler updateHandler;
	long updateuitime = 1000;
	
	@Override
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.configure);
		setupLastScan();
		setupButtons();
		setupSpinner();
		updateHandler = new Handler();
	}
	
	
	void setupLastScan() {
		lastScan = (TextView) findViewById(R.id.lastscan);
	}
	
	Runnable updateLastScan = new Runnable() {
		public void run() {
			if (lastScan != null) {
	    		String timeinpast = "";
	    		long lastscantime = ProcessMonitorReciever.getLastScan(ConfigureActivity.this);
	    		if (lastscantime < 0)
	    			lastScan.setText("No previous scans");
	    		long diff = System.currentTimeMillis() - lastscantime;
	    		if (diff < 1000 * 60) 
	    			timeinpast = (diff / 1000) + " seconds ago";
	    		else if (diff < 1000 * 60 * 60)
	    			timeinpast = (diff / 1000 / 60) + " minutes and " + ( (diff / 1000)%60 ) + " seconds ago";
	    		else 
	    			timeinpast = (diff / 1000 / 60 / 60) + " hours and " + ( (diff / 1000 / 60 )%60 ) + " minutes ago";
	    		lastScan.setText("Last scan: "+timeinpast);
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
    
    
    void setupSpinner() {
    	Spinner spinner = (Spinner) findViewById(R.id.scantime);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.scantimes_text_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        final int[] values = ConfigureActivity.this.getResources().getIntArray(R.array.scantimes_integer_array);
        long interval = ProcessMonitorReciever.getInterval(this);
        int intervalint = (int)(interval/1000);
        for (int i=0; i<values.length; i++) 
        	if (intervalint == values[i]) 
        		spinner.setSelection(i);

    	spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (arg2 >= 0 && arg2 <= values.length) {
					Log.d("ConfigureActivity", "Setting interval to:"+values[arg2]*1000);
					ProcessMonitorReciever.setInterval(ConfigureActivity.this, values[arg2]*1000);
					ProcessMonitorReciever.checkAndStartStop(ConfigureActivity.this);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
    		
		});
    }


	
	void setupButtons() {
		globalenabled = (CheckBox) findViewById(R.id.checkboxall);
		allapps = (CheckBox) findViewById(R.id.monitoring_allapps);
		softevents = (CheckBox) findViewById(R.id.monitoring_appevents);
		runningapps = (CheckBox) findViewById(R.id.monitoring_runningapps);
		processes = (CheckBox) findViewById(R.id.monitoring_processes);
		location = (CheckBox) findViewById(R.id.monitoring_location);
		wifi = (CheckBox) findViewById(R.id.monitoring_wifi);
		bluetooth = (CheckBox) findViewById(R.id.monitoring_bluetooth);

		setChecked();

		globalenabled.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				ProcessMonitorReciever.setEnabled(ConfigureActivity.this, isChecked);
				ProcessMonitorReciever.checkAndStartStop(ConfigureActivity.this);
				setChecked();
			}
		});
		allapps.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				ProcessMonitor.getSingleton().setRunning(ConfigureActivity.this, isChecked);
				ProcessesMonitor.getSingleton().setRunning(ConfigureActivity.this, isChecked);
				AppEventsMonitor.getSingleton().setRunning(ConfigureActivity.this, isChecked);
				setChecked();
			}
		});
		location.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				WifiMonitor.getSingleton().setRunning(ConfigureActivity.this, isChecked);
				BluetoothMonitor.getSingleton().setRunning(ConfigureActivity.this, isChecked);
				setChecked();
			}
		});
		runningapps.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				ProcessMonitor.getSingleton().setRunning(ConfigureActivity.this, isChecked);
				setChecked();
			}
		});
		processes.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				ProcessesMonitor.getSingleton().setRunning(ConfigureActivity.this, isChecked);
				setChecked();
			}
		});
		softevents.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				AppEventsMonitor.getSingleton().setRunning(ConfigureActivity.this, isChecked);
				setChecked();
			}
		});

		wifi.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				WifiMonitor.getSingleton().setRunning(ConfigureActivity.this, isChecked);
				setChecked();
			}
		});
		bluetooth.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				WifiMonitor.getSingleton().setRunning(ConfigureActivity.this, isChecked);
				setChecked();
			}
		});

	}

	void setChecked() {
		Log.d("ConfigureActivity", "Setting checked...");
		globalenabled.setChecked(ProcessMonitorReciever.isEnabled(this));
		runningapps.setChecked(ProcessMonitor.getSingleton().isRunning(this));
		processes.setChecked(ProcessesMonitor.getSingleton().isRunning(this));
		softevents.setChecked(AppEventsMonitor.getSingleton().isRunning(this));
		wifi.setChecked(WifiMonitor.getSingleton().isRunning(this));
		bluetooth.setChecked(BluetoothMonitor.getSingleton().isRunning(this));
		
		allapps.setChecked(runningapps.isChecked() && processes.isChecked() && softevents.isChecked());
		location.setChecked(wifi.isChecked() && bluetooth.isChecked());
		
		setAllEnabled(ProcessMonitorReciever.isEnabled(this));
	}	
	
	void setAllEnabled(boolean enabled) {
		runningapps.setEnabled(enabled);
		processes.setEnabled(enabled);
		softevents.setEnabled(enabled);
		wifi.setEnabled(enabled);
		bluetooth.setEnabled(enabled);
		
		allapps.setEnabled(enabled);
		location.setEnabled(enabled);
	}
	
}
