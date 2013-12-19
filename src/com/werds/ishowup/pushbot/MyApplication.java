package com.werds.ishowup.pushbot;

import com.pushbots.push.Pushbots;
import android.app.Application;

public class MyApplication extends Application {
	
	final String SENDER_ID = "329438371720";
	final String APP_ID = "52b36fda4deeae496b000010";
	
	@Override
	public void onCreate() {
		super.onCreate();
		Pushbots.init(this, SENDER_ID, APP_ID);
	}
}