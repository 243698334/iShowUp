package com.werds.ishowup.validation;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.werds.ishowup.course.TodaysClass;
import com.werds.ishowup.dbcommunication.DatabaseReader;

public class Validator extends Service{

	//private long currTime;
	private String qrCodeData;
	//private String secretKey = ""; // Fetch from database.
	
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
	
	public Validator(String qrCodeData) throws IOException {
		//this.currTime = System.currentTimeMillis() / 1000;
		this.qrCodeData = new String(qrCodeData);
		// StrictMode.enableDefaults();
	}
	
	private String fetchSecretKey(String crn, long qrCodeGenerateEpochTime) {
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
		String secretKey = null;
		try {
			secretKey = mFetch.execute(parameters, values).get();
		} catch (Exception e) {
			Log.d("1", "");
		}
		return secretKey;
	}
	
	public class FetchSecretKeyTask extends AsyncTask<ArrayList<String>, Void, String> {

		@Override
		protected String doInBackground(ArrayList<String>... arrLists) {
			ArrayList<String> parameters = arrLists[0];
			ArrayList<String> values = arrLists[1];
			
			DatabaseReader secretKeyFetcher = new DatabaseReader(DATABASE_SECRETKEY_URL);
			String secretKey = new String(secretKeyFetcher.fetchDataByString(parameters, values));
			return secretKey;
		}
	}
	
	/**
	 * @return a boolean array. Index 0: Overall validation status. 
	 * 							Index 1: Location validation status.
	 * 							Index 2: Time validation status.
	 * 							Index 3: SecretKey validation status.
	 * @return null if QR Code is not JSON formatted
	 * @throws JSONException
	 */
	public boolean[] validate() throws JSONException {
		TodaysClass todaysClass = new TodaysClass(qrCodeData);
		
		
		boolean[] status = new boolean[4];
		
		// Check location
		LocationTracker userLocation = new LocationTracker(this);
		double userLatitude = userLocation.getLatitude();
		double userLongitude = userLocation.getLongitude();
		double classLatitude = todaysClass.getLatitude();
		double classLongitude = todaysClass.getLongitude();
		status[1] = Math.abs(userLatitude - classLatitude) <= LOCATION_DIFFERENCE_TOLERANCE &&
				Math.abs(userLongitude - classLongitude) <= LOCATION_DIFFERENCE_TOLERANCE;
		
		// Check time
		long userTime = System.currentTimeMillis() / 1000;
		long qrCodeGenerateTime = todaysClass.getQRCodeGenerateEpochTime();
		status[2] = Math.abs(userTime - qrCodeGenerateTime) <= TIME_DIFFERENCE_TOLERANCE;
		
		// Check secret key
		String userSecretKey = new String(todaysClass.getSecretKey());
		String classSecretKey = new String(fetchSecretKey(todaysClass.getCRN(), todaysClass.getQRCodeGenerateEpochTime()));
		status[3] = userSecretKey.equals(classSecretKey);
		
		// Check overall	
		status[0] = status[1] && status[2] && status[3];
		return status;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
