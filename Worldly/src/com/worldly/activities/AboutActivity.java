package com.worldly.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import com.example.worldly.R;

@SuppressLint("NewApi")
public class AboutActivity extends Activity {

	private ImageView logo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		logo = (ImageView) findViewById(R.id.usaf_logo);
		logo.setScaleX(0.50f); 
		logo.setScaleY(0.50f);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.about, menu);
		return true;
	}

}
