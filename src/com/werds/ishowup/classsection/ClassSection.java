/**
 * 
 */
package com.werds.ishowup.classsection;

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
	
	public void setTitle(String title, int number, String section) {
		this.title = new String(title);
		this.number = number;
		this.section = new String(section);
	}
	
	public void setLocation() {}
}
