package com.werds.ishowup.ui;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.werds.ishowup.R;
import com.werds.ishowup.dbcommunication.DatabaseReader;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	/* Login php of the database server */
	private static final String DATABASE_LOGIN_PHP = "http://web.engr.illinois.edu/~ishowup4cs411/cgi-bin/login.php";
	private static final String SIGNUP_PHP = "http://web.engr.illinois.edu/~ishowup4cs411/cgi-bin/register.php";


	private String mNetID;
	private String mPassword;

	// UI references.
	private EditText mNetIDView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;

	// For saved account data.
	private SharedPreferences sp;
	private CheckBox auto_login;

	// Buttons
	private Button sign_up_btn;
	private Button sign_in_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);

		// Get instances.
		mNetIDView = (EditText) findViewById(R.id.netid);
		mPasswordView = (EditText) findViewById(R.id.password);
		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);
		auto_login = (CheckBox) findViewById(R.id.cb_auto);
		sign_up_btn = (Button) findViewById(R.id.sign_up_button);
		sign_in_btn = (Button) findViewById(R.id.sign_in_button);
		
		sp = this.getSharedPreferences("userInfo", MODE_PRIVATE);

		// Auto sign in ON by default.
		/*
		 * auto_login.setChecked(true); sp.edit().putBoolean("AUTO_CHECKED",
		 * false).commit();
		 */

		// If last time login successfully, the VERIFIED field should be true,
		// Then you are free to auto login. YAY!
		if (sp.getBoolean("VERIFIED", false)
				&& sp.getBoolean("AUTO_CHECKED", false)) {
			// Default to be auto login.
			auto_login.setChecked(true);

			Intent main_intent = new Intent(LoginActivity.this,
					MainActivity.class);
			LoginActivity.this.startActivity(main_intent);

		}

		// Set up the login form.
		mNetIDView.setText(mNetID);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		// Set clicklistener for sign in btn.
		sign_in_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptLogin();
			}
		});

		// Set clicklistener for sign up btn.
		/*
		 * final Intent signup_intent = new Intent();
		 * signup_intent.setAction("android.intent.action.VIEW"); Uri
		 * content_url = Uri
		 * .parse("http://web.engr.illinois.edu/~ishowup4cs411/register.php");
		 * signup_intent.setData(content_url);
		 * 
		 * sign_up_btn.setOnClickListener( new Button.OnClickListener() { public
		 * void onClick(View v) { startActivity(signup_intent); } });
		 */

		
		sign_up_btn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
			}
		});

		// Set Checkbox listener
		auto_login.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (auto_login.isChecked()) {
					sp.edit().putBoolean("AUTO_CHECKED", true).commit();
				} else {
					sp.edit().putBoolean("AUTO_CHECKED", false).commit();
				}
			}
		});
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mNetIDView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mNetID = mNetIDView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid NetID
		if (TextUtils.isEmpty(mNetID)) {
			mNetIDView.setError(getString(R.string.error_field_required));
			focusView = mNetIDView;
			cancel = true;
		} else if (mNetID.length() > 8) {
			mNetIDView.setError(getString(R.string.error_invalid_email));
			focusView = mNetIDView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		/**
		 * This login process runs on a separate thread.
		 * 
		 * @author KevinC
		 */
		@Override
		protected Boolean doInBackground(Void... params) {
			Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("netid", mNetID);
            parameters.put("password", mPassword);
            DatabaseReader login = new DatabaseReader(DATABASE_LOGIN_PHP);
            String loginStatus = new String(login.performRead(parameters));
            if (loginStatus.equals("Accept\n")) {
            	Map<String, String> sectionLookupParam = new HashMap<String, String>();
                parameters.put("netid", mNetID);
                parameters.put("operation", "lookup");

                DatabaseReader sectionLookUp = new DatabaseReader(SIGNUP_PHP);
                String sectionLookUpInfo = new String(sectionLookUp.performRead(parameters));
                try {
                	JSONObject signUpInfoJson = new JSONObject(sectionLookUpInfo);
                	String signUpStatus = signUpInfoJson.getString("Status");
                	String firstName = signUpInfoJson.getString("FirstName");
                	if (signUpStatus.equals("VALID")) {
                    	// Store all sections found for this student
                		JSONArray sections = signUpInfoJson.getJSONArray("Sections");
                		Set<String> allSections = new HashSet<String>();
                		for (int i = 0; i < sections.length(); i++) {
                			allSections.add(sections.getString(i).replace('_', ' '));
                			//Log.d("allSections"+i, sections.getString(i).replace('_', ' '));
                		}
                		sp.edit().putStringSet("allSections", allSections).commit();
                		sp.edit().putString("FirstName", firstName).commit();;
                		return true;
                    } else 
                    	return true;
                } catch (JSONException e) {
                	return true;
                }
            } else return false;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) {
				startActivity(new Intent(LoginActivity.this, MainActivity.class));
				// Should better use intent. (use putExtra() to share login
				// status)
				sp.edit().putBoolean("VERIFIED", true).commit();
				sp.edit().putString("NetID", mNetID).commit();
				finish(); // What to do if login successfully
			} else {
				mPasswordView
						.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}

}
