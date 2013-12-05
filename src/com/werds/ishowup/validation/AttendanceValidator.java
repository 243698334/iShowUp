package com.werds.ishowup.validation;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import com.werds.ishowup.course.TodaysClass;
import com.werds.ishowup.dbcommunication.DatabaseReader;

public class AttendanceValidator extends Service {

	private String qrCodeData;
	
	private double latitude;
	private double longitude;
	private long epochTime;
	private String secretKey;
		
	// The URL for the PHP script to fetch the secret key.
	private static final String DATABASE_SECRETKEY_URL = "http://web.engr.illinois.edu/~ishowup4cs411/cgi-bin/secretkey.php";
	// The tolerance of the time difference in seconds between the generation and validation of the QR code.
	private static final double TIME_DIFFERENCE_TOLERANCE = 300;
	// The tolerance of the distance difference in percentage between the building and the user's location. 
	private static final double LOCATION_DIFFERENCE_TOLERANCE = 0.01;
	
	public enum Status {
		SUCCESS, FAILED_LOCATION, FAILED_TIME, FAILED_SECRETKEY, 
		FAILED_LOCATION_TIME, FAILED_LOCATION_SECRETFAILED_BOTH
	}
	
	public AttendanceValidator(String qrCodeData) throws IOException {
		this.qrCodeData = new String(qrCodeData);
		// StrictMode.enableDefaults();
	}
	
	private void fetchLocation() {
		LocationTracker mLocation = new LocationTracker(this);
		if (mLocation.locationAvailable()) {
			this.latitude = mLocation.getLatitude();
			this.longitude = mLocation.getLongitude();
		} else {
			this.latitude = this.longitude = 0.0;
		}
	}
	
	private void fetchEpochTime() {
		this.epochTime = System.currentTimeMillis() / 1000;
	}
	
	private void fetchSecretKey(String crn, long qrCodeGenerateEpochTime) {
		/*Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");
		String todaysDate = sdf.format(cal.getTime());
		// For testing purpose, manually set todaysDate to be the ones needed.
		todaysDate = "11062013";*/
		
		SimpleDateFormat sdfDate = new SimpleDateFormat("MMddyyyy");
		String dateOfClass = sdfDate.format(new Date(qrCodeGenerateEpochTime));
		
		ArrayList<String> parameters = new ArrayList<String>();
		parameters.add("crn");
		parameters.add("date");
		ArrayList<String> values = new ArrayList<String>();
		values.add(crn);
		values.add(dateOfClass);
		
		FetchSecretKeyTask mFetch = new FetchSecretKeyTask();
		try {
			this.secretKey = new String(mFetch.execute(parameters, values).get());
		} catch (Exception e) {
			this.secretKey = null;
		}
	}
	
	public class FetchSecretKeyTask extends AsyncTask<ArrayList<String>, Void, String> {

		@Override
		protected String doInBackground(ArrayList<String>... arrLists) {
			ArrayList<String> parameters = arrLists[0];
			ArrayList<String> values = arrLists[1];
			
			DatabaseReader secretKeyFetcher = new DatabaseReader(DATABASE_SECRETKEY_URL);
			//return secretKeyFetcher.fetchDataByString(parameters, values);
			return null;
		}
	}
	
	/**
	 * @return a boolean array. Index 0: Overall validation status. 
	 * 							Index 1: Valid TodaysClass object
	 * 							Index 2: Location validation status.
	 * 							Index 3: Time validation status.
	 * 							Index 4: SecretKey validation status
	 * @return null if QR Code is not JSON formatted
	 * @throws JSONException
	 */
	public boolean[] validate() throws JSONException {
		boolean[] status = new boolean[5];
		for (int i = 0; i < status.length; i++) {
			status[i] = false;
		}
		
		// Check valid TodaysClass object
		TodaysClass todaysClass = new TodaysClass(qrCodeData);
		boolean[] importStatus = todaysClass.importTodaysClass();
		if (importStatus[0] == false) {
			return status;
		} else {
			status[1] = true;
		}
		
		// Check location
		fetchLocation();
		double classLatitude = todaysClass.getLatitude();
		double classLongitude = todaysClass.getLongitude();
		status[2] = Math.abs(this.latitude - classLatitude) <= LOCATION_DIFFERENCE_TOLERANCE &&
				Math.abs(this.longitude - classLongitude) <= LOCATION_DIFFERENCE_TOLERANCE;
		
		// Check time
		fetchEpochTime();
		long qrCodeGenerateTime = todaysClass.getQRCodeGenerateEpochTime();
		status[3] = Math.abs(this.epochTime - qrCodeGenerateTime) <= TIME_DIFFERENCE_TOLERANCE;
		status[3] = true;
		
		// Check secret key
		fetchSecretKey(todaysClass.getCRN(), todaysClass.getQRCodeGenerateEpochTime());
		String userSecretKey = new String(todaysClass.getSecretKey());
		status[4] = userSecretKey.equals(this.secretKey);
		
		// Check overall	
		status[0] = status[1] && status[2] && status[3] && status[4];
		return status;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
