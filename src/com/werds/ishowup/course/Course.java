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

	protected JSONObject courseInfo;

	public Course(String qrCodeData) throws JSONException {
		importCourse(qrCodeData);
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

	private void importCourse(String sectionData) throws JSONException {
		courseInfo = new JSONObject(sectionData);
		JSONObject courseTitle = courseInfo.getJSONObject("Title");
		JSONObject courseLocation = courseInfo.getJSONObject("Location");
		JSONArray courseGeoCoords = courseLocation.getJSONArray("Coordinates");
		JSONObject courseTime = courseInfo.getJSONObject("Time");
		JSONArray courseWeekdays = courseTime.getJSONArray("WeekDays");

		setTitle(courseTitle.getString("crn"),
				courseTitle.getString("Subject"),
				courseTitle.getString("SubjectAbbr"),
				courseTitle.getInt("Number"), courseTitle.getString("Section"));

		setLocation(courseLocation.getString("Building"),
				courseLocation.getString("Room"), courseGeoCoords.getDouble(0),
				courseGeoCoords.getDouble(1));

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
		setTime(courseTime.getInt("StartTime"), courseTime.getInt("EndTime"),
				weekdays);
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
