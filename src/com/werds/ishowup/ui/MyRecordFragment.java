package com.werds.ishowup.ui;

import java.util.LinkedHashSet;
import java.util.Set;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.werds.ishowup.R;

public class MyRecordFragment extends Fragment {

	private static SharedPreferences sp;
	private static String[] allSectionsArray;
	
	public static final String TAG = MyRecordFragment.class.getSimpleName();
	private static String courseTitle;
	private static String courseNo;

	public MyRecordFragment() {
	}

	public static MyRecordFragment newInstance() {
		return new MyRecordFragment();
	}

	//private ListView list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_my_record,
				container, false);

		sp = getActivity().getSharedPreferences("userInfo", 0);
		Set<String> allSections = sp.getStringSet("allSections", new LinkedHashSet<String>());
		allSectionsArray = allSections.toArray(new String[0]);
		/*list = (ListView) rootView.findViewById(R.id.list);
		list.setAdapter(new SimpleAdapter(getActivity(), getData(),
				R.layout.item, new String[] { "record_date", "record_status" },
				new int[] { R.id.record_date, R.id.record_status }));
*/
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		ViewPager mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
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
}
