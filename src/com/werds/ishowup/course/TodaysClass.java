package com.werds.ishowup.course;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.format.Time;

import java.util.*;

public class TodaysClass extends Course {

	private Date todaysDate;
	private long qrCodeGenerateEpochTime;
	private String secretKey;
	
	public TodaysClass(String courseData) throws JSONException {
		super(courseData);
		importTodaysClass();
	}

	private void importTodaysClass() throws JSONException {
		this.todaysDate = new Date();
		JSONObject qrCodeInfo = super.courseInfo.getJSONObject("QRCodeInfo");
		this.qrCodeGenerateEpochTime = qrCodeInfo.getInt("GenTime");
		this.secretKey = new String(qrCodeInfo.getString("SecretKey"));
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
