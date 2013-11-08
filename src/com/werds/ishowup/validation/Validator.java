package com.werds.ishowup.validation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import com.werds.ishowup.course.TodaysClass;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;

public class Validator extends Service{

	//private long currTime;
	private String qrCodeData;
	//private String secretKey = ""; // Fetch from database.
	
	// The URL for the PHP script to fetch the secret key.
	private static final String DATABASE_URL = "http://web.engr.illinois.edu/~ishowup4cs411/";
	// The tolerance of the time difference between the generation and validation of the QR code.
	private static final double TIME_DIFFERENCE_TOLERANCE = 1000;
	// The tolerance of the distance difference in percentage between the building and the user's location. 
	private static final double LOCATION_DIFFERENCE_TOLERANCE = 0.01;
	
	public enum Status {
		SUCCESS, FAILED_LOCATION, FAILED_TIME, FAILED_SECRETKEY, 
		FAILED_LOCATION_TIME, FAILED_LOCATION_SECRETFAILED_BOTH
	}
	
	public Validator(String qrCodeData) throws IOException {
		//this.currTime = System.currentTimeMillis() / 1000;
		this.qrCodeData = new String(qrCodeData);
		StrictMode.enableDefaults();
	}
	
	private String fetchSecretKey(String course) {
		// String course determines which database to be queried. 
		String secretKey = "";
		InputStream isr = null; 
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://web.engr.illinois.edu/~ishowup4cs411/" + course + "_secretkey.php");
			HttpResponse HttpResponse = httpClient.execute(httpPost);
			HttpEntity HttpEntity = HttpResponse.getEntity();
			isr = HttpEntity.getContent();
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection "+e.toString());
		}
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(isr, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			isr.close();
			secretKey = sb.toString();
		} catch (Exception e) {
			Log.e("log_tag", "Error  converting result "+e.toString());
		}
		return secretKey;
	}
	
	/**
	 * @return a boolean array. Index 0: Overall validation status. 
	 * 							Index 1: Location validation status.
	 * 							Index 2: Time validation status.
	 * 							Index 3: SecretKey validation status.
	 * @throws JSONException
	 */
	public boolean[] validate() throws JSONException {
		boolean[] status = new boolean[4];
		TodaysClass todaysClass = new TodaysClass(qrCodeData);
		
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
		long qrCodeGenTime = todaysClass.getQRCodeGenerateTime();
		status[2] = Math.abs(userTime - qrCodeGenTime) <= TIME_DIFFERENCE_TOLERANCE;
		
		// Check secret key
		String userSecretKey = new String(todaysClass.getSecretKey());
		String classSecretKey = new String(fetchSecretKey(todaysClass.getCourseID()));
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
