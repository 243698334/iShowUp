package com.werds.ishowup.course;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.format.Time;

import java.util.*;

public class TodaysClass extends Course {

	private Date todaysDate;
	private long qrCodeGenerateEpochTime;
	private String secretKey;
	
	public TodaysClass(String qrCodeData) {
		super(qrCodeData);
		this.todaysDate = new Date();
	}

	private void setQRCodeInfo(long qrCodeGenerateEpochTime, String secretKey) {
		this.qrCodeGenerateEpochTime = qrCodeGenerateEpochTime;
		this.secretKey = new String(secretKey);
	}
	
	/**
	 * @return a boolean array. Index 0: Overall import status. 
	 * 							Index 1: Valid JSON String.
	 * 							Index 2: Valid title.
	 * 							Index 3: Valid location.
	 * 							Index 4: Valid time.
	 *							Index 5: Valid QRCode Info.
	 */
	public boolean[] importTodaysClass() {
		boolean[] superStatus = super.importCourse();
		boolean[] status = new boolean[6];
		for (int i = 0; i < superStatus.length; i++) {
			status[i] = superStatus[i];
		}
		status[5] = false;
		if (superStatus[1] == false) {
			return status;
		}
		
		// Valid QRCodeInfo
		try {
			JSONObject qrCodeInfo = super.courseInfo.getJSONObject("QRCodeInfo");
			long genTime = qrCodeInfo.getInt("GenTime");
			String secretKey = qrCodeInfo.getString("SecretKey");
			setQRCodeInfo(genTime, secretKey);
			status[5] = true;
		} catch (JSONException e) {}
		
		status[0] = status[1] && status[2] && status[3] && status[4] && status[5];
		return status;
	}

	public long getQRCodeGenerateEpochTime() {
		return qrCodeGenerateEpochTime;
	}
	
	public Time getQRCodeGenerateTime() {
		return null;
	}
	
	public Date getTodaysClassDate() {
		return todaysDate;
	}

	public String getSecretKey() throws JSONException {
		return secretKey;
	}
	
}
