package com.worldly.data_store;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.worldly.data_models.Country;

import android.os.Environment;
import android.util.Log;

/**
 * This class handles the caching of data to disk and also the procedure to read
 * data from an existing cached file.
 * 
 * @author Annie the Eagle & Team
 * 
 */
public class CachingEngine
{
	/**
	 * Constant representing the directory where the data will be cached.
	 */
	private static final File WORKING_DIRECTORY = new File(
			Environment.getExternalStorageDirectory() + "/Worldly/");

	/**
	 * Constant representing the name of the file where the data of all
	 * countries will be saved to.
	 */
	private static final String COUNTRIES_FILENAME = "/countries.dat";

	/**
	 * Constant representing the separator used whilst writing to files.
	 */
	private static final String SEPARATOR = ", ", NEWLINE = "\n";

	/**
	 * Static method which writes the entire data of all countries from an
	 * ArrayList to a file in the device's external storage.
	 * 
	 * @param allCountries
	 *            : An ArrayList containing the entire data of all countries.
	 * @return TRUE if the operation has been successful, FALSE otherwise.
	 */
	public static boolean writeCountriesCache(ArrayList<Country> allCountries)
	{
		// Tries to save the data to a file in the device's external storage
		try
		{
			// Creates the directory in which the file will be stored
			WORKING_DIRECTORY.mkdirs();

			// Creates the file and opens an OutputStream to begin writing to it
			File cacheFile = new File(WORKING_DIRECTORY + COUNTRIES_FILENAME);
			FileOutputStream os = new FileOutputStream(cacheFile);

			// Writes the "expiry date" of the data to the header of the file
			os.write(("" + (System.currentTimeMillis() + 86400000 + NEWLINE))
					.getBytes());

			String data;

			// Iterates through the ArrayList of countries
			for (Country aCountry : allCountries)
			{
				// Places the country's data to a temporary String object
				data = aCountry.getId() + SEPARATOR;
				data += aCountry.getName() + SEPARATOR;
				data += aCountry.getIso2Code() + SEPARATOR;
				data += aCountry.getCapitalCity() + SEPARATOR;
				data += aCountry.getLatitude() + SEPARATOR;
				data += aCountry.getLongitude() + NEWLINE;

				// Writes the country's data to the file
				os.write(data.getBytes());
			}

			Log.d("CachingEngine", "Written " + allCountries.size()
					+ " countries to file");

			// Flushes and closes the OutputStream object
			os.flush();
			os.close();

			return true;
		}
		// If the system encountered a problem whilst creating the Working
		// Directory or whilst writing the data to the file
		catch (IOException ex)
		{
			Log.d("CachingEngine", "Not Writing to File");
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * Static method which reads the entire data of all countries from a file in
	 * the device's external storage and populates an ArrayList with the data.
	 * 
	 * @return An ArrayList containing the entire data of all countries.
	 */
	public static ArrayList<Country> getCachedCountries()
	{
		// Creates and initialises a File object to store the countries data
		File cacheFile = new File(WORKING_DIRECTORY + COUNTRIES_FILENAME);

		if (cacheFile.exists())
		{
			// Tries to read the countries data from the cache file
			try
			{
				// Prepares the objects to read the contents of cacheFile
				ArrayList<Country> cachedCountries = new ArrayList<Country>();
				FileInputStream is = new FileInputStream(cacheFile);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));

				// Gets the "expiry date" of the cache file
				Long expiryDateMillis = Long.parseLong(reader.readLine());

				// If the cached data is NOT out of date
				if (System.currentTimeMillis() <= expiryDateMillis)
				{
					String line;

					// Iterates through the cached file creating Country objects
					while ((line = reader.readLine()) != null)
						cachedCountries.add(new Country(line));

					// Adds an entry to LogCat describing the current status
					Log.i("CacheEngine", "Read " + cachedCountries.size()
							+ " countries from cache");
				}
				// If the cached data is out of date
				else
				{
					// Adds an entry to LogCat describing the current status
					Log.w("CacheEngine",
							"Cached countries out of date, refreshing...");
				}

				// Closes the BufferedReader and InputStream objects
				reader.close();
				is.close();

				return cachedCountries;
			}
			// If the system encountered a problem whilst opening the cache file
			// or whilst reading the file
			catch (IOException ex)
			{
				Log.d("CachingEngine", "Unable to read data from cache.");
				return new ArrayList<Country>();
			}
		}
		return new ArrayList<Country>();
	}
}
