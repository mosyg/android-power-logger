package edu.uiuc.cs.processmonitor;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class ProcessService extends Service {	
    public class ProcessBinder extends Binder {
        ProcessService getService() {
            return ProcessService.this;
        }
    }

    
    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new ProcessBinder();

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return mBinder;
	}
	
	CheckThread runningThread;
	public long waittime = 1000 * 1; // 30 seconds
	public long writetime = 1000 * 60 * 5; //5 minutes
	
	
	class CheckThread extends Thread {
		public boolean keeprunning = true;
		@Override
		public void run() {
			while (keeprunning) {
		        Log.d("ProcessScanner", "starting scan of recent processes");
		        
		        
		        
		        
		        
			    try { Thread.sleep(waittime); } catch (InterruptedException e) {  }
			}
		}
	}
	
	public void setRefreshRate(long millis) {
		this.waittime = millis;
	}
	
	public void startScanning() {
		stopScanning();
		runningThread = new CheckThread();
		runningThread.start();
	}
	
	public boolean isScanning() {
		return (runningThread != null);
	}
	
	public void stopScanning() {
		if (runningThread != null) {
			runningThread.keeprunning = false;
			runningThread.interrupt();
		}
		runningThread = null;
	}
	
	public void requestScan() {
		if (runningThread != null) {
			runningThread.interrupt();
		}
	}
	

	
}
