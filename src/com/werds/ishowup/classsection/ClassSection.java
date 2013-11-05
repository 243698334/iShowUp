/**
 * 
 */
package com.werds.ishowup.classsection;

import org.json.*;

/**
 * @author KevinC
 *
 */
public class ClassSection {
	
	private String title; // e.g. Computer Science
	private String titleAbbr; // e.g. CS
	private int number; // e.g. 357
	private String section; //e.g. AL1
	
	private String building; // e.g. Siebel Center
	private String buildingAbbr; // e.g. SIEBL
	private String room; // e.g. 1404
	private double[] geoLoction; // idx 0 as Latitude, 1 as Longitude
	
	private int startTime; // epoch time
	private int endTime; // epoch time
	private boolean[] weekdays;
	
	public ClassSection(String classData) throws JSONException {
		importClass(classData);
	}
	
	public void setTitle(String title, int number, String section) {
		this.title = new String(title);
		this.number = number;
		this.section = new String(section);
	}
	
	public void setLocation(String building, String room, double[] geoLocation) {
		this.building = new String(building);
		this.room = new String(room);
		for (int i = 0; i < 2; i++) {
			this.geoLoction[i] = geoLocation[i];
		}
	}
	
	public void setTime(int startTime, int endTime, boolean[] weekdays) {
		this.startTime = startTime;
		this.endTime = endTime;
		for (int i = 0; i < 7; i++) {
			this.weekdays[i] = weekdays[i];
		}
	}
	
	private void importClass(String sectionData) throws JSONException {
		JSONObject classInfo = new JSONObject(sectionData);
		JSONObject classTitle = classInfo.getJSONObject("Title");
		JSONObject classLocation = classInfo.getJSONObject("Location");
		JSONObject classTime = classInfo.getJSONObject("Time");
		JSONArray classWeekdays = classTime.getJSONArray("WeekDays");
		
		this.title = new String(classTitle.getString("Title"));
		this.number = classTitle.getInt("Number");
		this.section = new String(classTitle.getString("Section"));
		
		this.building = new String(classLocation.getString("Building"));
		this.room = new String(classLocation.getString("Room"));
		this.weekdays = new boolean[7];
		for (int i = 0; i < classWeekdays.length(); i++) {
			//switch ()
			// yay!
		}
		
	}
	
}
