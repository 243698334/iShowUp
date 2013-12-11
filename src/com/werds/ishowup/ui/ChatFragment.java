package com.werds.ishowup.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.werds.ishowup.R;

public class ChatFragment extends Fragment {

	public ChatFragment() {
	}

	private WebView chat_page;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_chat, container,
				false);

		chat_page = (WebView) rootView.findViewById(R.id.chat_page);
		WebSettings webSettings = chat_page.getSettings();
		webSettings.setJavaScriptEnabled(true);
		chat_page.loadUrl("http://web.engr.illinois.edu/~ishowup4cs411/chat.php");
		
		

		return rootView;
	}
}
