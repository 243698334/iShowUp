package com.werds.ishowup.ui.adapter;


import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.werds.ishowup.R;

public class GridCardAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<String> courseList;
	private ArrayList<String> sectionList;
	private ArrayList<String> statusList;
	private TextView course, section, status;
	private ImageView course_pic;

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
			gridView = inflater.inflate(R.layout.course, null);

			// set value into textview
			
			course = (TextView) gridView.findViewById(R.id.course_no);
			section = (TextView) gridView.findViewById(R.id.course_title);
			status = (TextView) gridView.findViewById(R.id.instructor);

			course.setText(courseList.get(position));
			section.setText(sectionList.get(position));
			status.setText(statusList.get(position));

			// set image based on selected text
			course_pic = (ImageView) gridView.findViewById(R.id.courseicon);
			if (courseList.get(position).equals("CS 411"))
				course_pic.setImageResource(R.drawable.cs411_pic);
			else if (courseList.get(position).equals("CS 511"))
				course_pic.setImageResource(R.drawable.cs511_pic);
			else if (courseList.get(position).equals("CS 461"))
				course_pic.setImageResource(R.drawable.cs461_pic);
			else if (courseList.get(position).equals("CS 418"))
				course_pic.setImageResource(R.drawable.cs418_pic);
			else if (courseList.get(position).equals("CS 440"))
				course_pic.setImageResource(R.drawable.cs440_pic);
			else 
				course_pic.setImageResource(R.drawable.temple_icon);
		} else {
			gridView = (View) convertView;
			//Button button = (Button)gridView.findViewById(R.id.overflowButton);
			
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
