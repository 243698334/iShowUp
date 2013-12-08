/**
 * 
 */
package com.werds.ishowup.course;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.format.Time;

/**
 * @author KevinC
 * 
 */
public class Course {

	private String crn; // e.g. 30109
	private String subject; // e.g. Computer Science
	private String subjectAbbr; // e.g. CS
	private int number; // e.g. 357
	private String section; // e.g. AL1

	private String building; // e.g. Siebel Center
	private String buildingAbbr; // e.g. SIEBL
	private String room; // e.g. 1404
	private double[] geoCoords; // idx 0 as Latitude, 1 as Longitude

	private int startTime; // epoch time
	private int endTime; // epoch time
	private boolean[] weekdays;

	protected String qrCodeData;
	protected JSONObject courseInfo;

	public Course(String qrCodeData) {
		this.qrCodeData = new String(qrCodeData);
	}

	private void setTitle(String crn, String subject, String subjectAbbr,
			int number, String section) {
		this.crn = new String(crn);
		this.subject = new String(subject);
		this.subjectAbbr = new String(subjectAbbr);
		this.number = number;
		this.section = new String(section);
	}

	private void setLocation(String building, String room, double latitude,
			double longitude) {
		this.building = new String(building);
		this.room = new String(room);
		this.geoCoords = new double[2];
		this.geoCoords[0] = latitude;
		this.geoCoords[1] = longitude;
	}

	private void setTime(int startTime, int endTime, boolean[] weekdays) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.weekdays = new boolean[7];
		for (int i = 0; i < 7; i++) {
			this.weekdays[i] = weekdays[i];
		}
	}

	/**
	 * @return a boolean array. Index 0: Overall import status. 
	 * 							Index 1: Valid JSON String.
	 * 							Index 2: Valid title.
	 * 							Index 3: Valid location.
	 * 							Index 4: Valid time.
	 * @param sectionData
	 * 
	 */
	public boolean[] importCourse() {
		boolean[] status = new boolean[5];
		for (int i = 0; i < status.length; i++) {
			status[i] = false;
		}
		
		// Valid JSON string
		try {
			this.courseInfo = new JSONObject(this.qrCodeData);
			status[1] = true;
		} catch (JSONException e) {
			return status;
		}
		
		// Valid title
		try {
			JSONObject courseTitle = this.courseInfo.getJSONObject("Title");
			String crn = courseTitle.getString("crn");
			String subject = courseTitle.getString("Subject");
			String subjectAbbr = courseTitle.getString("SubjectAbbr");
			int number = courseTitle.getInt("Number");
			String section = courseTitle.getString("Section");
			setTitle(crn, subject, subjectAbbr, number, section);
			status[2] = true;
		} catch (JSONException e) {}
		
		// Valid location
		try {
			JSONObject courseLocation = courseInfo.getJSONObject("Location");
			String building = courseLocation.getString("Building");
			String room = courseLocation.getString("Room");
			JSONArray courseGeoCoords = courseLocation.getJSONArray("Coordinates");
			double latitude = courseGeoCoords.getDouble(0);
			double longitude = courseGeoCoords.getDouble(1);
			setLocation(building, room, latitude, longitude);
			status[3] = true;
		} catch (JSONException e) {}
		
		// Valid time
		try {
			JSONObject courseTime = courseInfo.getJSONObject("Time");
			int startTime = courseTime.getInt("StartTime");
			int endTime = courseTime.getInt("EndTime");
			JSONArray courseWeekdays = courseTime.getJSONArray("WeekDays");
			boolean[] weekdays = new boolean[7];
			for (int i = 0; i < courseWeekdays.length(); i++) {
				if (courseWeekdays.getString(i).equals("Sun")) {
					weekdays[0] = true;
				} else if (courseWeekdays.getString(i).equals("Mon")) {
					weekdays[1] = true;
				} else if (courseWeekdays.getString(i).equals("Tue")) {
					weekdays[2] = true;
				} else if (courseWeekdays.getString(i).equals("Wed")) {
					weekdays[3] = true;
				} else if (courseWeekdays.getString(i).equals("Thu")) {
					weekdays[4] = true;
				} else if (courseWeekdays.getString(i).equals("Fri")) {
					weekdays[5] = true;
				} else if (courseWeekdays.getString(i).equals("Sat")) {
					weekdays[6] = true;
				}
			}
			setTime(startTime, endTime, weekdays);
			status[4] = true;
		} catch (JSONException e) {}
		
		status[0] = status[1] && status[2] && status[3] && status[4];
		return status;
	}

	public String getCRN() {
		return crn;
	}

	public boolean hasClassToday(Time currTime) {
		return weekdays[currTime.weekDay];
	}

	public int getStartTime() {
		return startTime;
	}

	public int getEndTime() {
		return endTime;
	}

	public double getLatitude() {
		return geoCoords[0];
	}

	public double getLongitude() {
		return geoCoords[1];
	}
}
