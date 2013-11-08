/**
 * 
 */
package com.werds.ishowup.course;

import java.util.Date;

import org.json.*;

/**
 * @author KevinC
 *
 */
public class Course {
	
	private String title; // e.g. Computer Science
	private String titleAbbr; // e.g. CS
	private int number; // e.g. 357
	private String section; //e.g. AL1
	
	private String building; // e.g. Siebel Center
	private String buildingAbbr; // e.g. SIEBL
	private String room; // e.g. 1404
	private double[] geoCoords; // idx 0 as Latitude, 1 as Longitude
	
	private int startTime; // epoch time
	private int endTime; // epoch time
	private boolean[] weekdays;
	
	protected JSONObject classInfo;
	
	public Course(String courseData) throws JSONException {
		importClass(courseData);
	}
	
	private void setTitle(String title, int number, String section) {
		this.title = new String(title);
		this.titleAbbr = new String(title);
		this.number = number;
		this.section = new String(section);
	}
	
	private void setLocation(String building, String room, double latitude, double longitude) {
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
	
	private void importClass(String sectionData) throws JSONException {
		classInfo = new JSONObject(sectionData);
		JSONObject classTitle = classInfo.getJSONObject("Title");
		JSONObject classLocation = classInfo.getJSONObject("Location");
		JSONArray classGeoCoords = classLocation.getJSONArray("Coordinates");
		JSONObject classTime = classInfo.getJSONObject("Time");
		JSONArray classWeekdays = classTime.getJSONArray("WeekDays");
		
		setTitle(classTitle.getString("Title"), classTitle.getInt("Number"), classTitle.getString("Section"));
		
		setLocation(classLocation.getString("Building"), classLocation.getString("Room"), classGeoCoords.getDouble(0), classGeoCoords.getDouble(1));

		boolean[] weekdays = new boolean[7];
		for (int i = 0; i < classWeekdays.length(); i++) {
			if (classWeekdays.getString(i).equals("Sun")) {
				weekdays[0] = true;
			} else if (classWeekdays.getString(i).equals("Mon")) {
				weekdays[1] = true;
			} else if (classWeekdays.getString(i).equals("Tue")) {
				weekdays[2] = true;
			} else if (classWeekdays.getString(i).equals("Wed")) {
				weekdays[3] = true;
			} else if (classWeekdays.getString(i).equals("Thu")) {
				weekdays[4] = true;
			} else if (classWeekdays.getString(i).equals("Fri")) {
				weekdays[5] = true;
			} else if (classWeekdays.getString(i).equals("Sat")) {
				weekdays[6] = true;
			}
		}
		setTime(classTime.getInt("StartTime"), classTime.getInt("EndTime"), weekdays);
	}
	
	public boolean hasClassToday(Date date) {
		return weekdays[date.getDay()];
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
	
	public String getCourseID() {
		return titleAbbr.toLowerCase() + new Integer(number).toString();
	}
}
