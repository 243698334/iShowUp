package com.werds.ishowup.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


public class ViewPagerActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getSupportFragmentManager().findFragmentByTag(MyRecordFragment.TAG) == null) {
			getSupportFragmentManager()
					.beginTransaction()
					.add(android.R.id.content, MyRecordFragment.newInstance(),
							MyRecordFragment.TAG).commit();
		}
	}
}
