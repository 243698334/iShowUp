package com.werds.ishowup.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.werds.ishowup.R;
import com.werds.ishowup.dbcommunication.DatabaseReader;
import com.werds.ishowup.ui.adapter.GridCardAdapter;

public class MyRecordFragment extends Fragment {

	private static SharedPreferences sp;
	private static String[] allSectionsArray;

	public static final String TAG = MyRecordFragment.class.getSimpleName();
	private static String courseTitle;
	private static String courseNo;

	private final String SIGNUP_PHP = "http://web.engr.illinois.edu/~ishowup4cs411/cgi-bin/register.php";
	
	//private PullToRefreshLayout mPullToRefreshLayout;
	private ViewPager mViewPager;

	public MyRecordFragment() {
	}

	public static MyRecordFragment newInstance() {
		return new MyRecordFragment();
	}

	// private ListView list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_myrecord, container,
				false);

		sp = getActivity().getSharedPreferences("userInfo", 0);
		Set<String> allSections = sp.getStringSet("allSections",
				new LinkedHashSet<String>());
		allSectionsArray = allSections.toArray(new String[0]);
		/*
		 * list = (ListView) rootView.findViewById(R.id.list);
		 * list.setAdapter(new SimpleAdapter(getActivity(), getData(),
		 * R.layout.item, new String[] { "record_date", "record_status" }, new
		 * int[] { R.id.record_date, R.id.record_status }));
		 */

		/*mPullToRefreshLayout = (PullToRefreshLayout) rootView
				.findViewById(R.id.ptr_layout);
		// Now setup the PullToRefreshLayout
		ActionBarPullToRefresh.from(getActivity())
		// Mark All Children as pullable
				.allChildrenArePullable()
				// Set the OnRefreshListener
				.listener(this)
				// Finally commit the setup to our PullToRefreshLayout
				.setup(mPullToRefreshLayout);*/

		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
		mViewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
	}

	public static class MyAdapter extends FragmentPagerAdapter {
		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return allSectionsArray.length;
		}

		@Override
		public Fragment getItem(int position) {
			Bundle args = new Bundle();
			args.putInt(ListViewFragment.POSITION_KEY, position);
			return ListViewFragment.newInstance(position);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return allSectionsArray[position];
		}

	}

	/*@Override
	public void onRefreshStarted(View view) {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				String netID = sp.getString("NetID", null);
				Set<String> allSections = new LinkedHashSet<String>();

				Map<String, String> sectionLookupParam = new HashMap<String, String>();
				sectionLookupParam.put("netid", netID);
				sectionLookupParam.put("operation", "lookup");

				DatabaseReader sectionLookUp = new DatabaseReader(SIGNUP_PHP);
				String sectionLookUpInfo = new String(
						sectionLookUp.performRead(sectionLookupParam));
				try {
					JSONObject signUpInfoJson = new JSONObject(
							sectionLookUpInfo);
					String signUpStatus = signUpInfoJson.getString("Status");
					String firstName = signUpInfoJson.getString("FirstName");
					if (signUpStatus.equals("VALID")) {
						JSONArray sections = signUpInfoJson
								.getJSONArray("Sections");
						for (int i = 0; i < sections.length(); i++) {
							allSections.add(sections.getString(i).replace('_',
									' '));
							Log.d("allSections" + i, sections.getString(i)
									.replace('_', ' '));
						}
						sp.edit().putStringSet("allSections", allSections)
								.commit();
					}
				} catch (JSONException e) {
					// nothing...
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				
				// Notify PullToRefreshLayout that the refresh has finished
				mViewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
				mPullToRefreshLayout.setRefreshComplete();
			}

			
		}.execute();

	}*/
}
