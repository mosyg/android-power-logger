package edu.uiuc.cs.processmonitor;

import java.io.IOException;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import edu.uiuc.cs.processmonitor.AppHistory.AppAggregates;
import edu.uiuc.cs.processmonitor.AppHistory.AppHistoryAggregateInfo;

public class AppVizActivity extends Activity {
	public final static String TAG = "AppVizActivity";
	AppHistory history;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainlist);
        new AppHistoryPopulator().execute();
    }
    
    
    public void startLoadingData() {
    }
    public void finishedLoadingData(AppHistory history) {
    	new AppAggregateTask().execute(history);
    }
    public void startVisualizingData() {
    	
    }
    public void finishedVisualizingData(AppAggregates viz) {
    	ListView list = (ListView)findViewById(R.id.mainlist);
    	AppVizAdapter adapt = new AppVizAdapter(this, 0, 0);
    	list.setAdapter(adapt);
    	adapt.setAppAggregate(viz);
    	
    	
    	
    	final PackageManager pm = this.getPackageManager();
    	for (AppHistoryAggregateInfo info : viz.apps) {
        	ApplicationInfo ai;
        	try {
        	    ai = pm.getApplicationInfo(info.packagename, 0);
        		adapt.add(info);
        	} catch (final NameNotFoundException e) {
        	    ai = null;
        	    e.printStackTrace();
        	}   		
        	
    	}
    	adapt.notifyDataSetChanged();
    	
    }
    
    public class AppHistoryPopulator extends AsyncTask<Void, Void, AppHistory> {
    	Dialog d;
    	
    	@Override
    	protected void onPreExecute() {
    		startLoadingData();
    		d = ProgressDialog.show(AppVizActivity.this, "Loading Data", "Just a moment!");
    	}
    	
		@Override
		protected AppHistory doInBackground(Void... arg0) {
			
			try {
				Log.d(TAG, "Staring ingestion");
				return DataManager.parseHistory(DataManager.getDataFile());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			Log.d(TAG, "Error! returning null!");
			return null;
		}
		
		@Override
		protected void onPostExecute(AppHistory result) {
			d.dismiss();
			if (result != null) {
				history = result;
			}
			Log.d(TAG, "Finished ingestion with: "+result);
			finishedLoadingData(result);
		}
    	
    }
    
    public class AppAggregateTask extends AsyncTask<AppHistory, Void, AppAggregates> {
    	Dialog d;
    	
    	@Override
    	protected void onPreExecute() {
    		startVisualizingData();
    		d = ProgressDialog.show(AppVizActivity.this, "Mining Data", "Just a moment!");
    	}
    	
		@Override
		protected AppAggregates doInBackground(AppHistory... arg0) {
			AppHistory app = arg0[0];
			return app.getAggregateInfo();
		}
		
		@Override
		protected void onPostExecute(AppAggregates result) {
			d.dismiss();
			Log.d(TAG, "Time awake vs asleep: "+result.timeawake  + " / "+result.totaltime + " "+ (100* (double)result.timeawake/(double)result.totaltime)+"%");
			for (AppHistoryAggregateInfo app : result.apps) {
				Log.d(TAG, ""+(((double)app.timeInForeground/(double)result.timeawake)*100d)+"%      App: "+app.packagename + " foreground:"+app.timeInForeground + " background:"+app.timeInBackground);
			}
			finishedVisualizingData(result);
		}
    	
    }
    
    
    
    
    
    
    public class AppVizAdapter extends ArrayAdapter<AppHistoryAggregateInfo> {
    	AppAggregates aggregates;
    	double maxtime = 0;
    	
    	public void setAppAggregate(AppAggregates agg) {
    		this.aggregates = agg;
    	}

		public AppVizAdapter(Context context, int resource,
				int textViewResourceId) {
			super(context, resource, textViewResourceId);
		}
		
		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
    		for (int i=0; i<this.getCount();i++) {
    			AppHistoryAggregateInfo info = getItem(i);
    			if (info.timeInForeground  > maxtime)
    				maxtime = info.timeInForeground;
    		}
		}
		
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.mainlist_item, parent, false);
			}
			AppHistoryAggregateInfo info = getItem(position);

	    	final PackageManager pm = AppVizActivity.this.getPackageManager();
    	    ApplicationInfo ai;
			try {
				ai = pm.getApplicationInfo(info.packagename, 0);
			} catch (NameNotFoundException e) {
				return convertView;
			}
    	    CharSequence name = pm.getApplicationLabel(ai);
			
			
			ImageView icon = (ImageView)convertView.findViewById(R.id.icon);
			TextView appname = (TextView)convertView.findViewById(R.id.appname);
			TextView percent = (TextView)convertView.findViewById(R.id.percentage);
			ProgressBar progress = (ProgressBar)convertView.findViewById(R.id.percentbar);
			
			icon.setImageDrawable(pm.getApplicationIcon(ai));
			appname.setText(name);
			double percentprogress = (100d*(double)info.timeInForeground/(double)aggregates.timeawake);
			percent.setText((""+Math.round(percentprogress))+"%");
			progress.setMax(1000);
			progress.setProgress((int)((double)info.timeInForeground / maxtime * 1000));
			
			return convertView;
		}
    	
    }
    
    
    
    
    
    
    
    
    
    
    
}