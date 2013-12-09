package com.werds.ishowup.validation;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.telephony.TelephonyManager;

import com.werds.ishowup.dbcommunication.DatabaseReader;

public class AttendanceValidator extends Service {

	private String netID;
	private String qrCodeData;
	
	private String crnFromQR;
	private String secretKeyFromQR;

	private double latitude;
	private double longitude;
	private long epochTime;
	
	// The URL for the PHP script to validate check-in process. 
	private static final String VALIDATOR_URL = "http://web.engr.illinois.edu/~ishowup4cs411/cgi-bin/validate.php";
	// The URL for the PHP script to fetch the student info. 
	private static final String CHECKININFO_URL = "http://web.engr.illinois.edu/~ishowup4cs411/cgi-bin/studentinfo.php";
	
	public AttendanceValidator(String netID, String qrCodeData) {
		this.netID = new String(netID);
		this.qrCodeData = new String(qrCodeData);
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
	
	private String getDeviceID() {
		//TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		//return telephonyManager.getDeviceId();
		return "kevins-macbook-pro";
	}
	
	
	private void fetchEpochTime() {
		this.epochTime = System.currentTimeMillis() / 1000;
	}
	
	/**
	 * Validate the Check-in process.
	 * @return 
	 * @throws JSONException 
	 */
	public String validateCheckIn() {
		JSONObject qrCodeJson = null;
		try {
			qrCodeJson = new JSONObject(this.qrCodeData);
			this.crnFromQR = qrCodeJson.getString("CRN");
			this.secretKeyFromQR = qrCodeJson.getString("SecretKey");
		} catch (JSONException e) {
			return "INVALID_QRCODE";
		}
		
		String status = null;
		Map<String, String> parameters= new HashMap<String, String>();
		parameters.put("netid", netID);
		parameters.put("crn", crnFromQR);
		parameters.put("secretkey", secretKeyFromQR);
		parameters.put("latitude", Double.toString(latitude));
		parameters.put("longitude", Double.toString(longitude));
		parameters.put("deviceid", getDeviceID());
		ValidateCheckInTask mValidate = new ValidateCheckInTask();
		try {
			status = mValidate.execute(parameters).get();
		} catch (Exception e) {
			status = null;
		}
		if (status.indexOf("INVALID_SECRETKEY") != -1) {
			return secretKeyFromQR;
		}
		return status;
	}
	
	private class ValidateCheckInTask extends AsyncTask<Map<String, String>, Void, String> {

		@Override
		protected String doInBackground(Map<String, String>... arg) {
			Map<String, String> parameters = arg[0];
			DatabaseReader validateCheckIn = new DatabaseReader(VALIDATOR_URL);
			String status = validateCheckIn.performRead(parameters);
			return status;
		}
		
	}
	
	
	/**
	 * @return a String array. Index 0: NetID. 
	 * 						   Index 1: Last Name
	 * 						   Index 2: First Name
	 *                         Index 3: Total sections count
	 *                         Index 4: Attended sections count
	 *                         Index 5: Section display name
	 * @throws JSONException 
	 * 
	 */
	public String[] fetchCheckInInfo() throws JSONException {
		String[] studentInfo = null;
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("netid", netID);
		parameters.put("crn", "31602");
		FetchCheckInInfoTask mFetch = new FetchCheckInInfoTask();
		try {
			studentInfo = mFetch.execute(parameters).get();
		} catch (Exception e) {
			studentInfo = null;
		}
		return studentInfo;
	}
	
	private class FetchCheckInInfoTask extends AsyncTask<Map<String, String>, Void, String[]> {

		@Override
		protected String[] doInBackground(Map<String, String>... arg) {
			Map<String, String> parameters = arg[0];
			
			String[] checkInInfo = new String[6];
			for (int i = 0; i < checkInInfo.length; i++) {
				checkInInfo[i] = null;
			}
			DatabaseReader studentInfoFetcher = new DatabaseReader(CHECKININFO_URL);
			String studentInfoRaw = studentInfoFetcher.performRead(parameters);
			JSONObject checkInInfoJSON;
			String lastName = null, firstName = null, totalCount = null, attendCount = null;
			try {
				checkInInfoJSON = new JSONObject(studentInfoRaw);
				checkInInfo[0] = new String(netID);
				checkInInfo[1] = checkInInfoJSON.getString("LastName");
				checkInInfo[2] = checkInInfoJSON.getString("FirstName");
				checkInInfo[3] = checkInInfoJSON.getString("totalCount");
				checkInInfo[4] = checkInInfoJSON.getString("attenCount");
				checkInInfo[5] = checkInInfoJSON.getString("SectionName");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return checkInInfo;
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
	/*public boolean[] validate() throws JSONException {
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
	}*/

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
