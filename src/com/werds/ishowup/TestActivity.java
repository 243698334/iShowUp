package com.werds.ishowup;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class TestActivity extends Activity {

	private TextView scanContent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		IntentIntegrator scanIntegrator = new IntentIntegrator(this);
		scanIntegrator.initiateScan();
		//LocationTracker locationTracker = new LocationTracker(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test, menu);
		return true;
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	// retrieve scan result
    	IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
    	if (scanningResult != null) {
    		String retrievedScanContent = scanningResult.getContents();
    		//String retrievedScanFormat = scanningResult.getFormatName();
    		scanContent.setText(retrievedScanContent);
    		//scanFormat.setText("Format: " + retrievedScanFormat);
    	} else {
    		Toast toast = Toast.makeText(getApplicationContext(),  "Urr! Try again!", Toast.LENGTH_SHORT);
    		toast.show();
    	}		
    }

}
