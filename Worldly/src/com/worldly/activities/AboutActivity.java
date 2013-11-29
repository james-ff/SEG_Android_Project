package com.worldly.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.example.worldly.R;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * About Activity, displaying the team members and attributions.
 * @author James Bellamy & Team
 *
 */
public class AboutActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
	}

	/**
	 * Opens the World Bank attribution on the device's browser.
	 * @param view : The view the user has pressed.
	 */
	public void worldBankAttribution(View view) {
		showBrowserWithLink("http://go.worldbank.org/OJC02YMLA0");
	}
	
	/**
	 * Opens the Google Maps attribution page on the device's browser.
	 * @param view : The view the user has pressed.
	 */
	public void googleMapsAttribution(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Google Maps")
				.setMessage(GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(this));
		builder.create().show();
	}
	
	/**
	 * Opens the Google Graphs attribution page on the device's browser.
	 * @param view : The view the user has pressed.
	 */
	public void googleGraphAttribution(View view) {
		showBrowserWithLink("http://creativecommons.org/licenses/by/3.0/legalcode");
	}
	
	public void showBrowserWithLink(String uri) {
		Intent browsserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
		startActivity(browsserIntent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.about, menu);
		return true;
	}
}