package com.werds.ishowup.validation;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings.Secure;
import android.util.Log;

import com.werds.ishowup.dbcommunication.DatabaseCommunicator;

public class AttendanceValidator extends Service {

	private boolean validateStatus;
	private Map<String, Boolean> detailStatus = new HashMap<String, Boolean>();
	
	private String netID;
	private String qrCodeData;
	private String crnFromQR;
	private String secretKeyFromQR;

	private double latitude;
	private double longitude;
	private String androidID;
	
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
		LocationService mLocation = new LocationService();
		this.latitude = mLocation.getLatitude();
		this.longitude = mLocation.getLongitude();
		
		Log.d("Location", "Lat: " + latitude);
		Log.d("Location", "Lng: " + longitude);
		/*
		if (mLocation.locationAvailable()) {
			this.latitude = mLocation.getLatitude();
			this.longitude = mLocation.getLongitude();
		} else {
			this.latitude = this.longitude = 0.0;
		}*/
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		String androidID = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
		Log.d("getDeviceID", "DeviceID: "+androidID);
	}
	
	public Map<String, Boolean> getDetailStatus() {
		return this.detailStatus;
	}
	
	/**
	 * Validate the Check-in process.
	 * @return 
	 * @throws JSONException 
	 */
	public void validateCheckIn() {
		try {
			JSONObject qrCodeJson = new JSONObject(this.qrCodeData);
			this.crnFromQR = qrCodeJson.getString("CRN");
			this.secretKeyFromQR = qrCodeJson.getString("SecretKey");
			detailStatus.put("QRCode", true);
		} catch (JSONException e) {
			validateStatus = false;
			detailStatus.put("Status", false);
			detailStatus.put("QRCode", false);
			return;
		}
		
		Map<String, String> parameters= new HashMap<String, String>();
		parameters.put("netid", netID);
		parameters.put("crn", crnFromQR);
		parameters.put("secretkey", secretKeyFromQR);
		parameters.put("latitude", Double.toString(latitude));
		parameters.put("longitude", Double.toString(longitude));
		parameters.put("deviceid", androidID);
		DatabaseCommunicator mValidator = new DatabaseCommunicator(VALIDATOR_URL);
		String checkInStatusRaw = mValidator.execute(parameters);
		
		try {
			JSONObject checkInStatusJSON = new JSONObject(checkInStatusRaw);
			String overallStatus = checkInStatusJSON.getString("Status");
			if (overallStatus.equals("SUCCESS")) {
				this.validateStatus = true;
				detailStatus.put("Status", true);
				detailStatus.put("AlreadyCheckedIn", checkInStatusJSON.getBoolean("AlreadyCheckedIn"));
				detailStatus.put("ReturnValue", true);
				return;
			} else {
				this.validateStatus = false;
				detailStatus.put("Status", false);
				detailStatus.put("Time", checkInStatusJSON.getBoolean("Time"));
				detailStatus.put("Enrollment", checkInStatusJSON.getBoolean("Enrollment"));
				detailStatus.put("SectionReady", checkInStatusJSON.getBoolean("SectionReady"));
				detailStatus.put("Location", checkInStatusJSON.getBoolean("Location"));
				detailStatus.put("SecretKey", checkInStatusJSON.getBoolean("SecretKey"));
				detailStatus.put("DeviceID", checkInStatusJSON.getBoolean("DeviceID"));
				detailStatus.put("ReturnValue", true);
				return;
			}
		} catch (JSONException e) {
			this.validateStatus = false;
			detailStatus.put("Status", false);
			detailStatus.put("ReturnValue", false);
			return;
		}
	}
	
	/**
	 * @return a String array. NetID, Last Name, First Name, Section Display Name
	 * 						   Total sections count, Attended sections count 
	 * 
	 */
	@SuppressWarnings("null")
	public Map<String, String> fetchCheckInInfo() {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("netid", netID);
		parameters.put("crn", crnFromQR);
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
			checkInInfo.put("attendCount", checkInInfoJSON.getString("attendCount"));
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
