package com.werds.ishowup.ui;

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
			return 4;
		}

		@Override
		public Fragment getItem(int position) {
			Bundle args = new Bundle();
			args.putInt(ListViewFragment.POSITION_KEY, position);
			return ListViewFragment.newInstance(args);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			//return "Fragment # " + position;
			courseTitle = "CS ";
			switch(position) {
			case 0:
				courseNo = "411";
				break;
			case 1:
				courseNo = "511";
				break;
			case 2:
				courseNo = "357";
				break;
			case 3:
				courseNo = "225";
				break;
			default:
				break;
			}
			return courseTitle + courseNo;
		}

	}

	/*private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("record_date", "Nov 11 2013");
		map.put("record_status", R.drawable.check);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("record_date", "Nov 13 2013 ");
		map.put("record_status", R.drawable.check);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("record_date", "Dec 2 2013");
		map.put("record_status", R.drawable.cross);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("record_date", "Dec 10 2013");
		map.put("record_status", R.drawable.check);
		list.add(map);

		return list;
	}*/
}
