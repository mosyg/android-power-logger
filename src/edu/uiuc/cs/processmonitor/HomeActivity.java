package edu.uiuc.cs.processmonitor;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class HomeActivity extends Activity {

	@Override
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		setupButtons();
	};
	
	void setupButtons() {
		((ImageButton)findViewById(R.id.govisualize)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this, VisualizeActivity.class);
				startActivity(intent);
			}
		});
		((ImageButton)findViewById(R.id.goupload)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this, UploadActivity.class);
				startActivity(intent);
			}
		});
		((ImageButton)findViewById(R.id.goconfigure)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this, ConfigureActivity.class);
				startActivity(intent);
			}
		});
		((ImageButton)findViewById(R.id.gosyslogs)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this, EventLogTestActivity.class);
				startActivity(intent);
			}
		});
	}
}
