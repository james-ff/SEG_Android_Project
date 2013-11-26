package com.worldly.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.worldly.R;
import com.worldly.controller.WorldlyController;
import com.worldly.data_store.ListOfIndicators;

/**
 * This activity lets the user pick the desired mode of search within the
 * application structure, it then saves the user's option in a global shared
 * preferences for later use by other activities.
 * 
 * @author Rafael da Silva Costa & Team
 * 
 */
class SelectionActivity extends Activity
{

	private WorldlyController appController;
	private Activity self = this;

	/**
	 * Constant representing the key used to store the selected mode in the
	 * shared preferences file.
	 *//*
	static final String MODE = "MODE";

	*//**
	 * Constant representing the name of the shared preferences file to be used
	 * across this application's activities.
	 *//*
	public static final String PREFS_NAME = "GLOBAL_PREFS";*/

	/**
	 * Constant across all activities, it holds all indicators that are loaded
	 * into memory.
	 */
	/*private static ListOfIndicators mainIndicators;*/

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selection);
		appController = WorldlyController.getInstance();
		/*mainIndicators = new ListOfIndicators();*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.selection, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle item selection
		switch (item.getItemId())
		{
			case R.id.action_about:
				startActivity(new Intent(this, AboutActivity.class));
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * This method is called whenever the user presses one of the three buttons
	 * on the screen, it then detects the option selected and updates the global
	 * shared preferences file with the mode selected.
	 * 
	 * @param v
	 *            : The View object which the user interacted with.
	 */
	public void chooseMode(View v)
	{
		int option = 0;

		// Detects which button the user has pressed
		switch (v.getId())
		{
			case R.id.ltvFamily:
			case R.id.btnFamily:
				option = WorldlyController.FAMILY_MOVE;
				break;
			case R.id.ltvNewLife:
			case R.id.btnNewLife:
				option = WorldlyController.PERSONAL_MOVE;
				break;
			case R.id.ltvBusiness:
			case R.id.btnBusiness:
				option = WorldlyController.BUSINESS_MOVE;
				break;
			case R.id.ltvCustom:
			case R.id.btnCustom:
				option = WorldlyController.CUSTOM_MOVE;
				showIndicatorsDialog();
				break;
		}

		// If the user has not selected the custom option
		if (option != WorldlyController.CUSTOM_MOVE)
		{
			/*saveSharedPreferences(option);*/

			// Updates the Worldly Controller status and starts map activity
			appController.setCurrentMoveStatus(option, null);
			startActivity(new Intent(this, MapActivity.class));
		}
	}

	/**
	 * Saves the selected move status to the shared preferences.
	 * 
	 * @param option
	 *            : One of the four constants representing the move status.
	 */
	/*protected void saveSharedPreferences(int option)
	{
		// Creates and instantiates a shared preferences object
		SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, 0);

		// Creates an editor object to update the shared preferences
		final SharedPreferences.Editor editor = sharedPref.edit();

		// Saves the mode selected by the user onto the shared preferences file
		editor.putInt(MODE, option);
		editor.commit();
	}*/

	/**
	 * Displays a dialog for the user to select the custom indicators to be used
	 * in the application lifecycle.
	 */
	private void showIndicatorsDialog()
	{
		// Creates the objects necessary to display the categories dialog
		final List<String> selection = new ArrayList<String>();
		final String[] customCategories = ListOfIndicators
				.getCategoriesAsArray();

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select categories of interest")
				.setMultiChoiceItems(customCategories, null,
						new DialogInterface.OnMultiChoiceClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog,
									int index, boolean isChecked)
							{
								if (isChecked)
								{
									// If the user checked the item, add it to
									// the selected items
									selection.add(customCategories[index]);
								}
								else if (selection.contains(index))
								{
									// Else, if the item is already in the
									// array, remove it
									selection.remove(Integer.valueOf(index));
								}
							}
						})
				.setPositiveButton(R.string.done,
						new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int id)
							{
								// If the user has made a selection
								if (selection.size() > 0)
								{
									// Saves the move status
									appController.setCurrentMoveStatus(
											WorldlyController.CUSTOM_MOVE,
											selection);
									/*saveSharedPreferences(WorldlyController.CUSTOM_MOVE);*/

									// Starts the map activity
									startActivity(new Intent(self,
											MapActivity.class));
								}

								else
								{
									Toast.makeText(self,
											"Please make a selection",
											Toast.LENGTH_SHORT).show();
								}
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int id)
							{
								Toast.makeText(self, "Custom Search Cancelled",
										Toast.LENGTH_SHORT).show();
							}
						});
		builder.create().show();
	}
}