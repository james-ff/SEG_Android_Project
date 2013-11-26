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

public class AboutActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
	}

	public void worldBankAttribution(View view) {
		showBrowserWithLink("http://go.worldbank.org/OJC02YMLA0");
	}
	
	public void googleMapsAttribution(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Google Maps")
				.setMessage(GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(this));
		builder.create().show();
	}
	
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
