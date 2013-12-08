package com.werds.ishowup.ui;

import info.androidhive.slidingmenu.R;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class HomeFragment extends Fragment {

	public HomeFragment() {
	}

	private Button scan_btn;
	/******************* TEST *******************/
	Button suc_btn, fail_btn;
	private SharedPreferences sp;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_home, container,
				false);
		// You need to inflate the Fragment's view and call findViewById() on
		// the View it returns.

		scan_btn = (Button) rootView.findViewById(R.id.scan_btn);
		scan_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), ScanActivity.class));
			}
		});

		/******************* TEST *******************/
		sp = this.getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		suc_btn = (Button) rootView.findViewById(R.id.suc_btn);
		fail_btn = (Button) rootView.findViewById(R.id.fail_btn);
		suc_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				sp.edit().putBoolean("VALIDATED", true).commit();
				startActivity(new Intent(getActivity(), ValidateActivity.class));
			}
		});
		fail_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				sp.edit().putBoolean("VALIDATED", false).commit();
				startActivity(new Intent(getActivity(), ValidateActivity.class));
			}
		});

		return rootView;
	}
}
