package com.werds.ishowup.ui;

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

	private SharedPreferences sp;
	private ImageView icon;
	private Button return_btn;
	private TextView hello, youHave, courseInfo;
	private String netID;
	private String firstName = "";
	private String courseTitle, courseNo, section;
	
	private String test;
	
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

		AttendanceValidator validator = new AttendanceValidator(netID,
				qrCodeData);
		test=validator.validateCheckIn();
		Log.d("returnValue", test);
		if (test.equals("SUCCESS\n")) {
			// check in successfully
			icon.setBackgroundResource(R.drawable.check);
			hello.setText("Hello " + firstName);
			courseInfo.setText(courseTitle+" "+courseNo+" "+section);
			return_btn.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(ValidateActivity.this,
							MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				}
			});
		} else {
			// check in failed
			icon.setBackgroundResource(R.drawable.cross);
			hello.setText("Sorry " + firstName);
			youHave.setText("Your checked-in has failed to");
			courseInfo.setText(courseTitle+" "+courseNo+" "+section);
			return_btn.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(ValidateActivity.this,
							ScanActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				}
			});
			return_btn.setText("Try again");
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
