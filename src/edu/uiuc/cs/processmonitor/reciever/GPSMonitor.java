package edu.uiuc.cs.processmonitor.reciever;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.util.Log;
import edu.uiuc.cs.processmonitor.DataManager;

public class GPSMonitor extends Monitor  {
	public final static String TAG = "GPSMonitor";
	static GPSMonitor singleton;
	
	public static GPSMonitor getSingleton() {
		if (singleton == null)
			singleton = new GPSMonitor();
		return singleton;
	}
	
	public GPSMonitor() {
		super("GPS", "gps");
	}

	public
	JSONObject scan(Context context, Intent intent) {
		try {		
			Log.d(TAG, "querying cached GPS");
			JSONObject gpsinfo = new JSONObject();
			LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
			Location lastknown = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
			gpsinfo.put("latitude", lastknown.getLatitude());
			gpsinfo.put("longitude", lastknown.getLongitude());
			gpsinfo.put("altitude", lastknown.getAltitude());
			gpsinfo.put("accuracy", lastknown.getAccuracy());
			gpsinfo.put("speed", lastknown.getSpeed());
			gpsinfo.put("hasspeed", lastknown.hasSpeed());
			gpsinfo.put("lastknowntimestamp", lastknown.getTime());
			gpsinfo.put("provider", lastknown.getProvider());
			gpsinfo.put("bearing", lastknown.getBearing());
			gpsinfo.put("extras", lastknown.getExtras());

			Log.d(TAG, "Writing to file: "+gpsinfo);
			return DataManager.saveGPSScan(context, gpsinfo);
			//Log.d(TAG, "complete");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
		
	
}
