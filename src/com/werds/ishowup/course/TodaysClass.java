package com.werds.ishowup.course;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class TodaysClass extends Course {

	private Date todaysDate;
	private long qrCodeGenerateTime;
	private String secretKey;
	
	public TodaysClass(String courseData) throws JSONException {
		super(courseData);
		importTodaysClass();
	}

	private void importTodaysClass() throws JSONException {
		this.todaysDate = new Date();
		JSONObject qrCodeInfo = super.classInfo.getJSONObject("QRCodeInfo");
		qrCodeGenerateTime = qrCodeInfo.getInt("GenTime");
		secretKey = new String(qrCodeInfo.getString("SecretKey"));
	}

	public long getQRCodeGenerateTime() {
		return qrCodeGenerateTime;
	}
	
	public Date getTodaysClassDate() {
		return todaysDate;
	}

	public String getSecretKey() throws JSONException {
		return secretKey;
	}
	
}
