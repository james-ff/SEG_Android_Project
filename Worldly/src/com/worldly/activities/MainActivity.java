package com.worldly.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.worldly.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.worldly.data_models.Country;

/* Testing for Annie's Indicator
 import org.json.JSONArray;
 import android.os.StrictMode;
 import android.os.StrictMode.ThreadPolicy;
 import com.worldly.network.QuerySystem;
 */

@SuppressLint("NewApi")
public class MainActivity extends Activity {

	private Activity self;

	private ArrayList<Country> allCountries;
	private ArrayList<Country> allAvailableCountries;
	private ArrayList<Country> selectedCountries;
	private HashMap<Marker, Country> markerToCountry;

	private Spinner allAvailableCountriesSpinner;
	private Spinner allSelectedCountrySpinner;
	private GoogleMap map;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		self = this;

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		
		map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			@Override public void onInfoWindowClick(Marker marker) {
				Log.d(getClass().getName(), marker.getTitle());
				Country aCountry = markerToCountry.get(marker);
				Log.d(getClass().getName(), aCountry.getIso2Code());
				marker.hideInfoWindow();
			}
		});

		Thread aThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Log.v(getClass().getName(), "Starting Download");
					allCountries = Country.getAllCountries();
					allAvailableCountries.addAll(allCountries);
					plotCountriesOnMap();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		aThread.start();
		
		allAvailableCountries = new ArrayList<Country>();
		selectedCountries = new ArrayList<Country>();
		
		allAvailableCountriesSpinner = (Spinner) findViewById(R.id.my_country_spinner);
		allSelectedCountrySpinner = (Spinner) findViewById(R.id.spinner1);
		
		allAvailableCountriesSpinner.setAdapter(new ArrayAdapter<Country>(self, android.R.layout.simple_list_item_1, allAvailableCountries));
		allSelectedCountrySpinner.setAdapter(new ArrayAdapter<Country>(self, android.R.layout.simple_list_item_1, selectedCountries));

		hasGLES20();
	}

	public boolean hasGLES20() {
		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		ConfigurationInfo info = am.getDeviceConfigurationInfo();
		Log.d(getClass().getName(), (info.reqGlEsVersion >= 0x20000) ? "Has Open GL 2.0" : "Has not got Open GL 2.0");
		return info.reqGlEsVersion >= 0x20000;
	}

	public void plotCountriesOnMap() {
		self.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				markerToCountry = new HashMap<Marker, Country>();
				if (allCountries.size() > 0) {
					for (Country aCountry : allCountries) {
						if (aCountry.getLatitude() != null && aCountry.getLongitude() != null) {
							LatLng aLocation = new LatLng(aCountry.getLatitude(), aCountry.getLongitude());
							if (map != null) {
								MarkerOptions aMarkerOption = new MarkerOptions().title(aCountry.getName()).snippet(aCountry.getCapitalCity()).position(aLocation);
								Marker aMarker = map.addMarker(aMarkerOption);
								markerToCountry.put(aMarker, aCountry);
							}
						}
					}
				}
			}
		});
	}

//	public void setSpinnerList() {
//		self.runOnUiThread(new Runnable() {
//			@Override
//			public void run() {
//				List<String> countryNames = new ArrayList<String>();
//				for (Country aCountry : allCountries) {
//					if (aCountry.getLatitude() != null
//							&& aCountry.getLongitude() != null) {
//						countryNames.add(aCountry.getName());
//					}
//				}
//				ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
//						self, android.R.layout.simple_spinner_dropdown_item,
//						countryNames);
//				allAvailableCountriesSpinner.setAdapter(spinnerArrayAdapter);
//				allSelectedCountrySpinner.setAdapter(spinnerArrayAdapter);
//			}
//		});
//	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
