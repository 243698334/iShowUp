package com.werds.ishowup.ui;

import info.androidhive.slidingmenu.R;

import java.util.ArrayList;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.mkyong.android.adapter.ImageAdapter;

public class HomeFragment extends Fragment {

	public HomeFragment() {
	}

	private Button scan_btn;

	/******************* GridView TEST *******************/
	GridView gridView;
	
	ArrayList<String> COURSES_NO = new ArrayList<String>();
	ArrayList<String> COURSES_TITLE = new ArrayList<String>();
	ArrayList<String> INSTRUCTOR = new ArrayList<String>();

	boolean a = COURSES_NO.add("CS 411");
	boolean b = COURSES_NO.add("CS 357");
	boolean c = COURSES_NO.add("CS 440");
	boolean d = COURSES_NO.add("CS 418");
	boolean x = COURSES_NO.add("CS 461");
	boolean e = COURSES_TITLE.add("Database System");
	boolean f = COURSES_TITLE.add("Numerical Methods");
	boolean g = COURSES_TITLE.add("Artificial Intelligence");
	boolean h = COURSES_TITLE.add("Interactive Computer Graphics");
	boolean y = COURSES_TITLE.add("Computer Security I");
	boolean i = INSTRUCTOR.add("Kevin C.Chang");
	boolean j = INSTRUCTOR.add("David Semeraro");
	boolean k = INSTRUCTOR.add("Svetlana Lazebnik");
	boolean l = INSTRUCTOR.add("John C. Hart");
	boolean z = INSTRUCTOR.add("Rakesh Bobba");
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_home, container,
				false);
		// You need to inflate the Fragment's view and call findViewById() on
		// the View it returns.

		/******************* GridView *******************/
		gridView = (GridView) rootView.findViewById(R.id.gridview1);

		gridView.setAdapter(new ImageAdapter(getActivity(), COURSES_NO, COURSES_TITLE, INSTRUCTOR));

		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Toast.makeText(
						getActivity().getApplicationContext(),
						((TextView) v.findViewById(R.id.course_no))
								.getText(), Toast.LENGTH_SHORT).show();

			}
		});

		/******************* GridView *******************/

		scan_btn = (Button) rootView.findViewById(R.id.scan_btn);
		scan_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), ScanActivity.class));
			}
		});

		return rootView;
	}
}
