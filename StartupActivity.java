package com.ntosto.seaturtlereport;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;


public class StartupActivity extends Activity {
	
	WebView wv1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_startup);
		
		wv1 = (WebView) findViewById(R.id.infoText);
		
		// display the user instructions in a WebView so that we can easily format it using HTML
		// enable the scrollbar in xml so the user knows that it is scrollable
		// TO DO: move the content to the Internet so it's easily updatable
		String customHtml = getString(R.string.turtleInfo);
		   wv1.loadData(customHtml, "text/html", "UTF-8");
		   wv1.setBackgroundColor(0x00000000);
		
		findViewById(R.id.reportBtn).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// next, go to the data form activity
				Intent intent = new Intent(StartupActivity.this,DataFormActivity.class);
				StartupActivity.this.startActivity(intent);
				
			} //end of onClick
		
		}); //end of onClickListener
		
	} //end of onCreate

} //end of StarupActivity Class
