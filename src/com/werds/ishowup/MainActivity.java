package com.werds.ishowup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {

	private Button scanButton; 
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	//StrictMode.enableDefaults();
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scanButton = (Button)findViewById(R.id.button_Scan);
        scanButton.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    

	@Override
	public void onClick(View v) {
		System.out.println("click!");
		if (v.getId() == R.id.button_Scan) {
			MainActivity.this.startActivity(new Intent(MainActivity.this, ScanActivity.class));
		}
	}

}
