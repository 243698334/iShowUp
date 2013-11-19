package com.werds.ishowup;

import com.werds.ishowup.validation.LocationTracker;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class ValidateActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_validate);
		
		Bundle bundle = getIntent().getExtras();
		String qrCodeData = new String(bundle.getString("QRCodeData"));
		TextView text = (TextView) findViewById(R.id.text);
		text.setText(qrCodeData);
		Log.d("QRCodeData", qrCodeData);
		LocationTracker mlocation = new LocationTracker(this);
		text.setText(Double.toString(mlocation.getLatitude()));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.validate, menu);
		return true;
	}

}
