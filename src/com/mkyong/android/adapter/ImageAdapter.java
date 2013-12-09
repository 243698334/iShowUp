package com.mkyong.android.adapter;

import info.androidhive.slidingmenu.R;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<String> courseNoList;
	private ArrayList<String> courseTitleList;
	private ArrayList<String> instructorList;
	private TextView courseNo, courseTitle, instructor;
	private ImageView imageView;
	private HashMap<String, Integer> map;

	public ImageAdapter(Context context, ArrayList<String> courseNoList,
			ArrayList<String> courseTitleList, ArrayList<String> instructorList) {
		this.context = context;
		this.courseNoList = courseNoList;
		this.courseTitleList = courseTitleList;
		this.instructorList = instructorList;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View gridView;

		if (convertView == null) {

			gridView = new View(context);

			// get layout from mobile.xml
			gridView = inflater.inflate(R.layout.course, null);

			// set value into textview
			courseNo = (TextView) gridView.findViewById(R.id.course_no);
			courseTitle = (TextView) gridView.findViewById(R.id.course_title);
			instructor = (TextView) gridView.findViewById(R.id.instructor);

			courseNo.setText(courseNoList.get(position));
			courseTitle.setText(courseTitleList.get(position));
			instructor.setText(instructorList.get(position));

			// set image based on selected text
			imageView = (ImageView) gridView.findViewById(R.id.courseicon);

			map = new HashMap<String, Integer>();
			switch (position) {
			case 0:
				imageView.setImageResource(R.drawable.cs411_pic);
				break;
			case 1:
				imageView.setImageResource(R.drawable.cs357_pic);
				break;
			case 2:
				imageView.setImageResource(R.drawable.cs440_pic);
				break;
			case 3:
				imageView.setImageResource(R.drawable.cs418_pic);
				break;
			case 4:
				imageView.setImageResource(R.drawable.cs461_pic);
				break;
			default:
				imageView.setImageResource(R.drawable.temple_icon);
				break;
			}
			/*
			 * if (course.equals("Windows")) {
			 * imageView.setImageResource(R.drawable.windows_logo); } else if
			 * (course.equals("iOS")) {
			 * imageView.setImageResource(R.drawable.ios_logo); } else if
			 * (course.equals("Blackberry")) {
			 * imageView.setImageResource(R.drawable.blackberry_logo); } else {
			 * imageView.setImageResource(R.drawable.android_logo); }
			 */

		} else {
			gridView = (View) convertView;
		}

		return gridView;
	}

	@Override
	public int getCount() {
		return courseNoList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

}
