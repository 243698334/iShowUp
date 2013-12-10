package com.werds.ishowup.validation;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.werds.ishowup.dbcommunication.DatabaseCommunicator;

public class AttendanceValidator extends Service {

	private boolean validateStatus;
	
	private String netID;
	private String qrCodeData;
	
	private String crnFromQR;
	private String secretKeyFromQR;

	private double latitude;
	private double longitude;
	
	// The URL for the PHP script to validate check-in process. 
	private static final String VALIDATOR_URL = "http://web.engr.illinois.edu/~ishowup4cs411/cgi-bin/validate.php";
	// The URL for the PHP script to fetch the student info. 
	private static final String CHECKININFO_URL = "http://web.engr.illinois.edu/~ishowup4cs411/cgi-bin/studentinfo.php";
	
	public AttendanceValidator(String netID, String qrCodeData) {
		this.netID = new String(netID);
		this.qrCodeData = new String(qrCodeData);
		fetchLocation();
	}
	
	public boolean isValid() {
		return this.validateStatus;
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
	
	public String getDeviceID() {
		//TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		//return telephonyManager.getDeviceId();
		return "kevins-macbook-pro";
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
		
		Map<String, String> parameters= new HashMap<String, String>();
		parameters.put("netid", netID);
		parameters.put("crn", crnFromQR);
		parameters.put("secretkey", secretKeyFromQR);
		parameters.put("latitude", Double.toString(latitude));
		parameters.put("longitude", Double.toString(longitude));
		parameters.put("deviceid", getDeviceID());
		DatabaseCommunicator mValidator = new DatabaseCommunicator(VALIDATOR_URL);
		String status = mValidator.execute(parameters);
		this.validateStatus = status.indexOf("SUCCESS") != -1;
		return status;
	}
	
	
	
	/**
	 * @return a String array. NetID, Last Name, First Name, Section Display Name
	 * 						   Total sections count, Attended sections count
	 * 
	 * @throws JSONException 
	 * 
	 */
	@SuppressWarnings("null")
	public Map<String, String> fetchCheckInInfo() throws JSONException {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("netid", netID);
		parameters.put("crn", "31602");
		DatabaseCommunicator mFetcher = new DatabaseCommunicator(CHECKININFO_URL);
		String checkInInfoRaw =  mFetcher.execute(parameters);
		try {
			JSONObject checkInInfoJSON = new JSONObject(checkInInfoRaw);
			Map<String, String> checkInInfo = null;
			checkInInfo.put("NetID", netID);
			checkInInfo.put("LastName", checkInInfoJSON.getString("LastName"));
			checkInInfo.put("FirstName", checkInInfoJSON.getString("FirstName"));
			checkInInfo.put("SectionName", checkInInfoJSON.getString("SectionName"));
			checkInInfo.put("totalCount", checkInInfoJSON.getString("totalCount"));
			checkInInfo.put("attenCount", checkInInfoJSON.getString("attenCount"));
			return checkInInfo;
		} catch (JSONException e) {
			return null;
		}
	}
	


	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
