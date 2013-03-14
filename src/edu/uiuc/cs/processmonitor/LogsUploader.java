package edu.uiuc.cs.processmonitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;

public class LogsUploader {
	public final static String TAG = "Uploader";
	Context context;
	long uploadtime = 30000L;
	File dir;
	String url = "http://mosyg2.cs.uiuc.edu/~marsan1/android/something2.php";
	String imei;
	public LogsUploader(Context context, String url) {
		this.context = context;
		this.url = url;
		dir = DataManager.getDataDir();
		dir.mkdirs();
		TelephonyManager service = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		imei = service.getDeviceId();
	}
	
	
	
	public class UploadThread extends Thread {
		boolean keeprunning = true;
		public void run() {
			while (keeprunning) {
				uploadtime = 30000L;//context.getUploadInterval();
				Long curtime = System.currentTimeMillis();
				Log.d(TAG, "looking for files to upload (once ever "+uploadtime+" milliseconds)");
				if (uploadtime > 0) {
					scanForFilesAndUpload();
				} else {
					//-1 means we don't upload. so just wait 30 seconds
					uploadtime = 30000L;
				}
				Long timediff = System.currentTimeMillis()-curtime;
				if (timediff > 0) {
					 try {
							Thread.sleep(uploadtime-timediff);
					 } catch (Exception e) {
						 e.printStackTrace();
					 }
				}
			}
		}
	}
	
	UploadThread uploadThread;
	
	
	public void startBackgroundRecording() {
		uploadThread = new UploadThread();
		uploadThread.start();
		Log.d(TAG, "STARTING UPLOADING THREAD");
	}
	
	public void stopBackgroundRecording() {
		
		if (uploadThread == null) return;
		uploadThread.keeprunning = false;
		uploadThread = null;
		Log.d(TAG, "STOPPING UPLOADING THREAD");
	}
	
	
	public String[] filesToUpload() {
		return dir.list();
	}


	
	
	
	
	
	
	public String makeFilename(long milliseconds) {
		Intent i = context.getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		int level = i.getIntExtra("level", 0);
		int voltage = i.getIntExtra("voltage", 0);
		return imei+"_l-"+milliseconds+"ms_t-"+System.currentTimeMillis()+"_batt-l-"+level+"_batt-v-"+voltage+"___";
	}
	
	public void scanForFilesAndUpload() {
		for (File f : dir.listFiles()) {
			if (f.toString().endsWith(".json") || f.toString().endsWith(".txt")) {
				//upload it and delete it!
				Log.d(TAG, "Found file "+f+" uploading and deleting!");
				uploadFile(f, true);
			}
		}
	}
	

	
	
	public void uploadFile(final File file,final boolean deleteonfinish) {
		Log.d("UploadFile", "Uploading "+file);
		new Runnable() {
			public void run() {
				uploadFile_internal(file); 
				if (deleteonfinish) file.delete();
			}
		}.run();
	}
	
	
	
	public void uploadFile_internal(File NAME_OF_FILE) {

		try {
			FileInputStream fis = new FileInputStream(NAME_OF_FILE);
			HttpFileUploader htfu = new HttpFileUploader(url, "noparamshere", makeFilename(System.currentTimeMillis())+NAME_OF_FILE.getName());
			htfu.doStart(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	
}
