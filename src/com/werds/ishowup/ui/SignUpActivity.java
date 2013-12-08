package com.werds.ishowup.ui;

import info.androidhive.slidingmenu.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.webkit.WebView;

public class SignUpActivity extends Activity {

	private WebView sign_up_page;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		sign_up_page = (WebView) findViewById(R.id.sign_up_page);

		sign_up_page
				.loadUrl("http://web.engr.illinois.edu/~ishowup4cs411/register.php");
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
