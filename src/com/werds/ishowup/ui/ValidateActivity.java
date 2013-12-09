package com.werds.ishowup.ui;

import org.json.JSONException;

import info.androidhive.slidingmenu.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.werds.ishowup.validation.AttendanceValidator;

public class ValidateActivity extends Activity {

	private SharedPreferences sp;
	private ImageView icon;
	private TextView text;
	private String netID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_validate);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		//icon = (ImageView) findViewById(R.id.icon_status);
		text = (TextView) findViewById(R.id.status_text);
		sp = this.getSharedPreferences("userInfo", MODE_PRIVATE);
		netID = sp.getString("NetID", null);
		
		/******************* UNCOMMENT AFTER TEST *******************/
		Bundle bundle = getIntent().getExtras();
		String qrCodeData = new String(bundle.getString("QRCodeData"));
		/*text.setText(qrCodeData); 
		 
		LocationTracker mlocation = new LocationTracker(this);
		text.setText(Double.toString(mlocation.getLatitude()));*/

		/******************* TEST *******************/
		/******************* change conditions later *******************/

		if (sp.getBoolean("VALIDATED", false)) { // check in successfully
			text.setText(R.string.validate_success);
		} else { // check in fail
			text.setText(R.string.validate_fail);
		}
		
		AttendanceValidator validator = new AttendanceValidator(netID, qrCodeData);
		text.setText(validator.validateCheckIn());

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// Disable back button. (override to avoid unknown terminating)
	@Override
	public void onBackPressed() {
	}
	
}
