package edu.uiuc.cs.processmonitor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.EventLog;
import android.util.EventLog.Event;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class EventLogTestActivity extends Activity {
    /** Called when the activity is first created. */
	Button dumpbutton;
	TextView logview;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.systemlog);
        dumpbutton = (Button)findViewById(R.id.dumplogs);
        dumpbutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				testTheLogs();
			}
		});
        logview = (TextView) findViewById(R.id.logview);
    }
    
    
    public AsyncTask<Void, Void, Void> whatever = new AsyncTask<Void,Void,Void>() {

		@Override
		protected Void doInBackground(Void... params) {
			testTheLogs();
			return null;
		}
    		
    };
    
    
    public void testTheLogs() {
    	try {
	    	File f = new File("/system/etc/event-log-tags");
	    	File out = DataManager.getDataFile(DataManager.SYSTEM_EVENT_LOG);
	    	BufferedWriter w = new BufferedWriter(new FileWriter(out));
	    	BufferedReader r = new BufferedReader(new FileReader(f));
	    	String line;
	    	while ((line = r.readLine()) != null) {
	    		try {
	    			String[] split = line.split(" ");
	    			String name = split[0];
	    			int id = Integer.parseInt(name);
	    			Log.d("EventLogTest", "ROW: "+line);
	    			String o = "ROW: "+line+"\n";
		    		w.append(o);
		    		logview.append(o);

	    			writeLog(id, w);
	    		} catch (Exception ee) {
	    			ee.printStackTrace();
	    		}
	    	}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    public void writeLog(int id, BufferedWriter writer) {
    	String name = EventLog.getTagName(id);
    	writeLog(name, writer);
    }
    
    public void writeLog(String name, BufferedWriter writer) {
    	int tag = EventLog.getTagCode(name);
    	Collection<Event> events = new ArrayList<Event>();
    	try {
			EventLog.readEvents(new int[] {tag}, events);
	    	for (Event e : events) {
	    		Object o = e.getData();
	    		/** one of Integer, Long, String, null, or Object[] of same. **/
	    		String datatext = "";
	    		if (o instanceof Integer ) {
	    			datatext = "I. "+((Integer)o).toString();
	    		} else if (o instanceof Long) {
	    			datatext = "L. "+((Long)o).toString();
	    		} else if (o instanceof String) {
	    			datatext = "S. "+(o).toString();
	    		} else if (o instanceof Object[]) {
	    			datatext = "Object[]: [";
	    			Object[] oo = (Object[])o;
	    			for (Object subo : oo) {
	    	    		if (subo instanceof Integer ) {
	    	    			datatext += "i. "+((Integer)subo).toString();
	    	    		} else if (subo instanceof Long) {
	    	    			datatext += "l. "+((Long)subo).toString();
	    	    		} else if (subo instanceof String) {
	    	    			datatext += "s. "+(subo).toString();
	    	    		}
	    	    		datatext += ", ";
	    			}
	    			datatext += "]";
	    		}
	    		String out = String.format("Type: %s  -  Event: time: %s, tag: %s, pID: %s, threadID: %s, data: %s\n",name, e.getTimeNanos()+"", e.getTag()+"", e.getProcessId()+"", e.getTag()+"", datatext);
	    		Log.d("EventLogTest", out);
	    		writer.append(out);
	    		logview.append(out);
	    	}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

    }
}