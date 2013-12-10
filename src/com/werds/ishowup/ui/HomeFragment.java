package com.werds.ishowup.ui;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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

import com.werds.ishowup.R;
import com.werds.ishowup.dbcommunication.DatabaseCommunicator;
import com.werds.ishowup.dbcommunication.DatabaseReader;
import com.werds.ishowup.ui.adapter.GridCardAdapter;

public class HomeFragment extends Fragment implements OnRefreshListener{

	public HomeFragment() {
	}

	private final String CHECKIN_STATUS_URL = "http://web.engr.illinois.edu/~ishowup4cs411/cgi-bin/checkinstatus.php";
	
	private Button scan_btn;
	private TextView greeting;

	private SharedPreferences sp;
	private Set<String> allSections;
	private String netID;
	private String firstName;
	
	private PullToRefreshLayout mPullToRefreshLayout;
	
	/******************* GridView TEST *******************/
	GridView gridView;
	
	ArrayList<String> course = new ArrayList<String>();
	ArrayList<String> section = new ArrayList<String>();
	ArrayList<String> status = new ArrayList<String>();
	
	/**
	 * @return String GO_HEAD, NOT_READY, ALREADY_CHECKED_IN
	 */
	private String checkInStatus(String sectionFullName) {
		sectionFullName = sectionFullName.replace(' ', '_');
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("netid", netID);
		parameters.put("sectionfullname", sectionFullName);
		DatabaseCommunicator checkInStatusReader = new DatabaseCommunicator(CHECKIN_STATUS_URL);
		String checkInStatusRaw = checkInStatusReader.execute(parameters);
		try {
			JSONObject checkInStatusJSON = new JSONObject(checkInStatusRaw);
			return checkInStatusJSON.getString("Status");
		} catch (JSONException e) {
			return "FAILED";
		}
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		
		
		// Get All sections for this student from Shared Preference
		sp = getActivity().getSharedPreferences("userInfo", 0);
		allSections = sp.getStringSet("allSections", new HashSet<String>());
		netID = sp.getString("NetID", null);
		firstName = sp.getString("FirstName", null);
		//greeting = (TextView)findViewById(R.id.greeting);
		
		
		
		
		View rootView = inflater.inflate(R.layout.fragment_home, container, false);
		greeting = (TextView)rootView.findViewById(R.id.greeting);
		greeting.setText("What's up, " + firstName + "!");
		
		mPullToRefreshLayout = (PullToRefreshLayout)rootView.findViewById(R.id.ptr_layout);
		// Now setup the PullToRefreshLayout
		ActionBarPullToRefresh.from(getActivity())
	            // Mark All Children as pullable
	            .allChildrenArePullable()
	            // Set the OnRefreshListener
	            .listener(this)
	            // Finally commit the setup to our PullToRefreshLayout
	            .setup(mPullToRefreshLayout);
		
		
		
		for (Iterator<String> i = allSections.iterator(); i.hasNext(); ) {
			String currStrRaw = i.next();
			String currCourse = currStrRaw.substring(0, currStrRaw.lastIndexOf(' '));
			String currSection = currStrRaw.substring(currStrRaw.lastIndexOf(' ') + 1);
			String currStatus = checkInStatus(currStrRaw);
			
			if (currStatus.equals("GO_AHEAD")) {
				status.add("Go ahead and check-in!");
			} else if (currStatus.equals("NOT_READY")) {
				status.add("Not ready for check-in");
			} else if (currStatus.equals("ALREADY_CHECKED_IN")) {
				status.add("Already checked-in");
			}
			course.add(currCourse);
			section.add("Section " + currSection);
		}


		
		// You need to inflate the Fragment's view and call findViewById() on
		// the View it returns.

		/******************* GridView *******************/
		gridView = (GridView) rootView.findViewById(R.id.gridview1);

		gridView.setAdapter(new GridCardAdapter(getActivity(), course, section, status));

		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Toast.makeText(getActivity().getApplicationContext(),
						((TextView) v.findViewById(R.id.course_no)).getText(),
						Toast.LENGTH_SHORT).show();

			}
		});
		

		scan_btn = (Button) rootView.findViewById(R.id.scan_btn);
		scan_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), ScanActivity.class));
			}
		});
		
		return rootView;
	}


	@SuppressWarnings("unchecked")
	@Override
	public void onRefreshStarted(View view) {
		
		new AsyncTask<Void, Void, Void>() {
			
			@Override
			protected Void doInBackground(Void... params) {
				course = new ArrayList<String>();
				section = new ArrayList<String>();
				status = new ArrayList<String>();
				
				for (Iterator<String> i = allSections.iterator(); i.hasNext(); ) {
					String currStrRaw = i.next();
					String currCourse = currStrRaw.substring(0, currStrRaw.lastIndexOf(' '));
					String currSection = currStrRaw.substring(currStrRaw.lastIndexOf(' ') + 1);
					String currStatus = checkInStatusSingleThread(currStrRaw);
					
					if (currStatus.equals("GO_AHEAD")) {
						status.add("Go ahead and check-in!");
					} else if (currStatus.equals("NOT_READY")) {
						status.add("Not ready for check-in");
					} else if (currStatus.equals("ALREADY_CHECKED_IN")) {
						status.add("Already checked-in");
					}
					course.add(currCourse);
					section.add("Section " + currSection);
				}
				return null;
			}
			
			@Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);

                // Notify PullToRefreshLayout that the refresh has finished
                mPullToRefreshLayout.setRefreshComplete();
            }
			
			private String checkInStatusSingleThread(String sectionFullName) {
				sectionFullName = sectionFullName.replace(' ', '_');
				Map<String, String> parameters = new HashMap<String, String>();
				parameters.put("netid", netID);
				parameters.put("sectionfullname", sectionFullName);
				DatabaseReader checkInStatusReader = new DatabaseReader(CHECKIN_STATUS_URL);
				String checkInStatusRaw = checkInStatusReader.performRead(parameters);
				
				try {
					JSONObject checkInStatusJSON = new JSONObject(checkInStatusRaw);
					return checkInStatusJSON.getString("Status");
				} catch (JSONException e) {
					return "FAILED";
				}
			}
        }.execute();
        gridView.setAdapter(new GridCardAdapter(getActivity(), course, section, status));
	}
}
