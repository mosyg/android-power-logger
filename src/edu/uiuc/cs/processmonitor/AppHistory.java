package edu.uiuc.cs.processmonitor;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public class AppHistory {
	public final static byte FOREGROUND = 10;
	public final static byte BACKGROUND = 1;
	public final static byte NOTRUNNING = 0;
	
	public final long SLEEP_THRESHOLD = 6 * 60 * 1000;
	
	public byte[] history;
	public int length;
	public String[] packagenames;
	public HashMap<String,Integer> packagemap;
	public long[] timestamps;
	
	
	
	
	public static class AppHistoryAggregateInfo {
		String packagename;
		String appname;
		long timeInForeground;
		long timeInBackground;
		
		public AppHistoryAggregateInfo(String packagename) {
			this.packagename = packagename;
		}
	}
	
	public static class AppAggregates {
		AppHistoryAggregateInfo[] apps;
		long timeawake;
		long totaltime;
	}
	
	public AppAggregates getAggregateInfo() {
		return getAggregateInfo(timestamps[0], timestamps[timestamps.length-1]);
	}
	
	public AppAggregates getAggregateInfoBefore(long before) {
		return getAggregateInfo(timestamps[0], before);
	}
	public AppAggregates getAggregateInfoAfter(long after) {
		return getAggregateInfo(after, timestamps[timestamps.length-1]);
	}
	public AppAggregates getAggregateInfo(long timestart, long timeend) {
		int numpackages = packagenames.length;
		long totaltime = timeend - timestart;
		long timeawake = 0;
		AppHistoryAggregateInfo[] apps = new AppHistoryAggregateInfo[numpackages];
		
		for (int i=0; i<numpackages; i++) {
			apps[i] = new AppHistoryAggregateInfo(packagenames[i]);
		}
		
		int[] range = getIndexOfTimestampRange(timestart,timeend);
		int start = range[0]; int end = range[1];
		long lasttime = timestart;
		long timediff = 0;
		for (int i=start; i<end; i++) {
			long diff = timestamps[i] - lasttime;
			//if (Math.abs(diff) < SLEEP_THRESHOLD)
				timediff = diff;
			lasttime = timestamps[i];
			for (int j=0; j<numpackages; j++) {
				AppHistoryAggregateInfo app = apps[j];
				if (history[i*numpackages + j] == FOREGROUND) {
					app.timeInForeground += timediff;
				} else if (history[i*numpackages + j] == BACKGROUND) {
					app.timeInBackground += timediff;
				}
			}
			timeawake += diff;
		}
		
		AppAggregates aggregate = new AppAggregates();
		aggregate.apps = apps;
		Arrays.sort(apps, new Comparator<AppHistoryAggregateInfo>() {
			@Override
			public int compare(AppHistoryAggregateInfo object1,
					AppHistoryAggregateInfo object2) {
				if ( object1.timeInForeground  > object2.timeInForeground) 
					return -1;
				else if (object1.timeInForeground < object2.timeInForeground)
					return 1;
				return 0;
				
			}
		});
		aggregate.totaltime = totaltime;
		aggregate.timeawake = timeawake;
		return aggregate;
	}
	
	int[] getIndexOfTimestampRange(long timestart, long timeend) {
		int indexstart = Arrays.binarySearch(timestamps, timestart);
		int indexend = Arrays.binarySearch(timestamps, timeend);
		if (indexstart < 0) indexstart = -indexstart;
		if (indexend < 0) indexend = -indexend;
		return new int[] {indexstart, indexend};
	}
	
}
