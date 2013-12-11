package com.werds.ishowup.ui;

import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.werds.ishowup.R;
import com.werds.ishowup.validation.AttendanceValidator;

public class ValidateActivity extends Activity {

	private AttendanceValidator validator = null;
	
	private SharedPreferences sp;
	private ImageView icon;
	private Button return_btn;
	private TextView hello, youHave, courseInfo;
	private String netID;
	private String firstName = "";
	private String sectionDisplayName = "";
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_validate);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		icon = (ImageView) findViewById(R.id.checkin_status);
		return_btn = (Button) findViewById(R.id.return_btn);
		hello = (TextView) findViewById(R.id.hello);
		youHave = (TextView) findViewById(R.id.you_have);
		courseInfo = (TextView) findViewById(R.id.course_info);
		sp = this.getSharedPreferences("userInfo", MODE_PRIVATE);
		netID = sp.getString("NetID", null);

		Bundle bundle = getIntent().getExtras();
		String qrCodeData = new String(bundle.getString("QRCodeData"));

		firstName = sp.getString("FirstName", null);
		
		validator = new AttendanceValidator(netID, qrCodeData);
		validator.validateCheckIn();
		
		Log.d("ValidatorStatus", Boolean.toString(validator.isValid()));
		
		if (validator.isValid()) {
			onValidateSuccess();
			// check in successfully
		} else {
			onValidateFailed();
			// check in failed
		}

	}
	
	private void onValidateSuccess() {
		icon.setBackgroundResource(R.drawable.check);
		hello.setText("Hello " + firstName);
		courseInfo.setText(sectionDisplayName);
		return_btn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ValidateActivity.this,
						MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
		
		/**
		 * Get Extra info
		 */
		Map<String, Boolean> detailStatus = validator.getDetailStatus();
		boolean overallStatus = detailStatus.get("Status").booleanValue();
		boolean alreadyCheckedIn = detailStatus.get("AlreadyCheckedIn").booleanValue();
		Map<String, String> checkInInfo = validator.fetchCheckInInfo();
		if (checkInInfo != null) {
			this.sectionDisplayName = checkInInfo.get("SectionName");
			String totalCount = checkInInfo.get("totalCount");
			String attendCount = checkInInfo.get("attendCount");
		}
		
	}
	
	private void onValidateFailed() {
		icon.setBackgroundResource(R.drawable.cross);
		hello.setText("Sorry " + firstName);
		youHave.setText("Check in failed.");
		return_btn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ValidateActivity.this,
						ScanActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
		return_btn.setText("Try again");
		
		/**
		 * Get the reason of failure
		 */
		Map<String, Boolean> detailStatus = validator.getDetailStatus();
		if (detailStatus.get("QRCode")) {
			boolean overallStatus = detailStatus.get("Status").booleanValue();
			boolean timeStatus = detailStatus.get("Time").booleanValue();
			boolean sectionReadyStatus = detailStatus.get("SectionReady").booleanValue();
			boolean locationStatus = detailStatus.get("Location").booleanValue();
			boolean secretKeyStatus = detailStatus.get("SecretKey").booleanValue();
			boolean deviceIDStatus = detailStatus.get("DeviceID").booleanValue();
		}
		
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
