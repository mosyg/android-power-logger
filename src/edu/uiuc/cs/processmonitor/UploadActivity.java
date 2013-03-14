package edu.uiuc.cs.processmonitor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class UploadActivity extends Activity {
	
	
	Button uploadButton;
	LogsUploader uploader;
	
	@Override
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload);
		uploader = new LogsUploader(this, DataManager.URL);
		setupUploadButton();
	}
	
	
	public void setupUploadButton() {
		uploadButton = (Button) findViewById(R.id.uploadnow);
		
		uploadButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("UploadActivity", "Uploading lots of stuff!~");
				launchBackgroundUpload();
			}
		});
	}
	
	public LogsUploaderTask launchBackgroundUpload() {
		return (LogsUploaderTask)new LogsUploaderTask().execute();
	}
	public class LogsUploaderTask extends AsyncTask<Void,Void,Void> {
		ProgressDialog dialog;
		int items;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			items = uploader.filesToUpload().length;
			dialog = ProgressDialog.show(UploadActivity.this, "Uploading", String.format("uploading %d files...",items));
			dialog.setMax(items);
		}
		@Override
		protected Void doInBackground(Void... params) {
			uploader.scanForFilesAndUpload();
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			dialog.dismiss();
		}
	}

}
