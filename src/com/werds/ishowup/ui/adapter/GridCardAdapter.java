package com.werds.ishowup.ui.adapter;


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

public class GridCardAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<String> courseList;
	private ArrayList<String> sectionList;
	private ArrayList<String> statusList;
	private TextView course, section, status;
	private ImageView imageView;
	private HashMap<String, Integer> map;

	public GridCardAdapter(Context context, ArrayList<String> courseList,
			ArrayList<String> sectionList, ArrayList<String> statusList) {
		this.context = context;
		this.courseList = courseList;
		this.sectionList = sectionList;
		this.statusList = statusList;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View gridView;

		if (convertView == null) {

			gridView = new View(context);

			// get layout from mobile.xml
			gridView = inflater.inflate(R.layout.grid_card, null);

			// set value into textview
			
			course = (TextView) gridView.findViewById(R.id.course);
			section = (TextView) gridView.findViewById(R.id.section);
			status = (TextView) gridView.findViewById(R.id.status);

			course.setText(courseList.get(position));
			section.setText(sectionList.get(position));
			status.setText(statusList.get(position));

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
		//int count = courseList.size() - 1;
		//return count >= 0 ? count : 0;
		//return Math.min(courseList.size(), Math.min(sectionList.size(), statusList.size()))courseList.size();
		return courseList.size();
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
