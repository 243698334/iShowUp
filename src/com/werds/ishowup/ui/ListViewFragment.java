package com.werds.ishowup.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.werds.ishowup.R;

public class ListViewFragment extends Fragment {

	public static final String POSITION_KEY = "com.burnside.embeddedfragmenttest.POSITION";
	private ListView list;
	
	public static ListViewFragment newInstance(Bundle args) {
		ListViewFragment fragment = new ListViewFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_test, container, false);
		list = (ListView) rootView.findViewById(R.id.list);
		list.setAdapter(new SimpleAdapter(getActivity(), getData(),
				R.layout.item, new String[] { "record_date", "record_status" },
				new int[] { R.id.record_date, R.id.record_status }));
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		
	}
	
	private List<Map<String, Object>> getData() {
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
	}

}
