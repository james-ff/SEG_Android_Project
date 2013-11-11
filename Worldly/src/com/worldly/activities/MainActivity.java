package com.worldly.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.worldly.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
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
	
	private Spinner myCountrySpinner;
	private Spinner currentCountrySpinner;
	private GoogleMap map;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		self = this;

		myCountrySpinner = (Spinner) findViewById(R.id.my_country_spinner);
		currentCountrySpinner = (Spinner) findViewById(R.id.spinner1); //TODO: Change later
		
		map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
		
		Thread aThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Log.v(getClass().getName(), "Starting Download");
					allCountries = Country.getAllCountries();
					printAllCountries();
					plotCountriesOnMap();
					setSpinnerList();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		aThread.start();
		
		if (hasGLES20()) {
			Log.v(getClass().getName(), "Has Open GL 2.0");
		} else {
			Log.v(getClass().getName(), "Has not got Open GL 2.0");
		}
	}
	
	public boolean hasGLES20() {
	    ActivityManager am = (ActivityManager)
	                getSystemService(Context.ACTIVITY_SERVICE);
	    ConfigurationInfo info = am.getDeviceConfigurationInfo();
	    return info.reqGlEsVersion >= 0x20000;
	}

	public void printAllCountries() {
		for (Country aCountry : allCountries) {
			aCountry.print();
		}
	}
	
	public void plotCountriesOnMap() {
		self.runOnUiThread(new Runnable() {
			@Override public void run() {
				if (allCountries.size() > 0) {
					for (Country aCountry : allCountries) {
						if (aCountry.getLatitude() != null && aCountry.getLongitude() != null) {
							LatLng aLocation = new LatLng(aCountry.getLatitude(), aCountry.getLongitude());
							if (map != null) {
								map.addMarker(new MarkerOptions()
				                	.title(aCountry.getName())
				                	.snippet(aCountry.getCapitalCity())
				                	.position(aLocation));
							}
						}
					}
				}
			}
		});
	}
	
	public void setSpinnerList() {
		self.runOnUiThread(new Runnable() {
			@Override public void run() {
				List<String> countryNames = new ArrayList<String>();
				for (Country aCountry : allCountries) {
					if (aCountry.getLatitude() != null && aCountry.getLongitude() != null) {
						countryNames.add(aCountry.getName());
					}
				}
				ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(self, android.R.layout.simple_spinner_dropdown_item, countryNames);
				myCountrySpinner.setAdapter(spinnerArrayAdapter);
				currentCountrySpinner.setAdapter(spinnerArrayAdapter);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
