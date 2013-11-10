package com.werds.ishowup;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class ValidateActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_validate);
		
		Bundle bundle = getIntent().getExtras();
		String qrCodeData = bundle.getString("QRCodeData");
		Log.d("QRCodeData", qrCodeData);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.validate, menu);
		return true;
	}

}
