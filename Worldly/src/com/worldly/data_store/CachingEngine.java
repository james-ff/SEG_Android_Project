package com.worldly.data_store;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;
import android.util.Log;

import com.worldly.controller.WorldlyController;
import com.worldly.data_models.Country;

/**
 * This class handles the caching of data to disk and also the procedure to read
 * data from an existing cached file.
 * 
 * @author Ferdinand Keller & Rafael da Silva Costa & Team
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
	 * Constant representing the name of the file where the data will be saved.
	 */
	private static final String COUNTRIES_FILENAME = "/countries.dat",
			SELECTED_COUNTRIES_FILENAME = "/selectedcountries.dat",
			CURRENT_MOVE_STATUS_FILENAME = "/currentmovestatus.dat",
			CATEGORIES_FILENAME = "/categories.dat";

	/**
	 * Constant representing the separator used whilst writing to files.
	 */
	private static final String SEPARATOR = ", ", NEWLINE = "\n";

	/**
	 * Static method which writes the entire data of all countries from an
	 * ArrayList to a file in the device's external storage.
	 * 
	 * @param allCountries
	 *            : A List containing all Country objects.
	 * @return TRUE if the operation has been successful, FALSE otherwise.
	 */
	public static boolean writeCountriesCache(List<Country> allCountries)
	{
		long expiry = System.currentTimeMillis() + (24 * 60 * 60 * 1000);
		return writeToFile(COUNTRIES_FILENAME, expiry, allCountries);
	}

	/**
	 * Static method which writes the selected countries from an List to a file
	 * in the device's external storage.
	 * 
	 * @param countries
	 *            : A List containing the selected countries.
	 * @return TRUE if the operation has been successful, FALSE otherwise.
	 */
	public static boolean writeSelectedCountriesCache(List<Country> countries)
	{
		return writeToFile(SELECTED_COUNTRIES_FILENAME, Long.MAX_VALUE,
				countries);
	}

	/**
	 * Static method which writes the current move status to a file in the
	 * device's external storage.
	 * 
	 * @param currentMoveStatus
	 *            : One of the four constants representing the current move
	 *            status.
	 * @return TRUE if the operation has been successful, FALSE otherwise.
	 * @see WorldlyController#BUSINESS_MOVE
	 * @see WorldlyController#CUSTOM_MOVE
	 * @see WorldlyController#FAMILY_MOVE
	 * @see WorldlyController#PERSONAL_MOVE
	 */
	public static boolean writeCurrentMoveStatusCache(int currentMoveStatus)
	{
		List<String> data = new ArrayList<String>();
		data.add(Integer.toString(currentMoveStatus));
		return writeToFile(CURRENT_MOVE_STATUS_FILENAME, Long.MAX_VALUE, data);
	}

	/**
	 * Static method which writes the List of categories to a file in the
	 * device's external storage.
	 * 
	 * @param categories
	 *            : A List containing the categories.
	 * @return TRUE if the operation has been successful, FALSE otherwise.
	 */
	public static boolean writeCategoriesCache(List<String> categories)
	{
		return writeToFile(CATEGORIES_FILENAME, Long.MAX_VALUE, categories);
	}

	/**
	 * Static method which reads all Country objects from a file in the device's
	 * external storage and populates a List with the data.
	 * 
	 * @return A List containing all Country objects.
	 */
	public static List<Country> getCachedCountries()
	{
		return readFromFile(COUNTRIES_FILENAME);
	}

	/**
	 * Static method which reads the selected Country objects from a file in the
	 * device's external storage and populates a List with the data.
	 * 
	 * @return A List containing all Country objects.
	 */
	public static List<Country> getCachedSelectedCountries()
	{
		return readFromFile(SELECTED_COUNTRIES_FILENAME);
	}

	/**
	 * Static method which reads the current move status from a file in the
	 * device's external storage returns its value as an integer.
	 * 
	 * @return One of the four constants representing the current move status,
	 *         or -1 if the cache file does not exist.
	 * @see WorldlyController#BUSINESS_MOVE
	 * @see WorldlyController#CUSTOM_MOVE
	 * @see WorldlyController#FAMILY_MOVE
	 * @see WorldlyController#PERSONAL_MOVE
	 */
	public static int getCachedCurrentMoveStatus()
	{
		List<String> temp = readFromFile(CURRENT_MOVE_STATUS_FILENAME);
		return (temp.size() > 0) ? Integer.valueOf(temp.get(0)) : -1;
	}

	/**
	 * Static method which reads the categories move status from a file in the
	 * device's external storage returns its value as an integer.
	 * 
	 * @return A List containing the categories.
	 */
	public static List<String> getCachedCategories()
	{
		return readFromFile(CATEGORIES_FILENAME);
	}

	/**
	 * Writes the specified data to a file with the specified filename in the
	 * device's external storage with the specified expiry date in milliseconds.
	 * 
	 * @param filename
	 *            : One of the four contants representing the filename.
	 * @param expiry
	 *            : The expiry date of the cache file in milliseconds.
	 * @param data
	 *            : The data to be written onto the file.
	 * @see #CATEGORIES_FILENAME
	 * @see #COUNTRIES_FILENAME
	 * @see #CURRENT_MOVE_STATUS_FILENAME
	 * @see #SELECTED_COUNTRIES_FILENAME
	 */
	private static <E> boolean writeToFile(String filename, long expiry,
			List<E> data)
	{
		// Tries to save the data to a file in the device's external storage
		try
		{
			// Creates the directory in which the file will be stored
			WORKING_DIRECTORY.mkdirs();
						
			// Creates the file and opens an OutputStream to begin writing to it
			File cacheFile = new File(WORKING_DIRECTORY + filename);
			FileOutputStream fos = new FileOutputStream(cacheFile);
			BufferedOutputStream bos = new BufferedOutputStream(fos);

			// Writes the "expiry date" of the data to the header of the file
			fos.write(("" + (expiry + NEWLINE)).getBytes());

			String temp = new String();

			// If data being parsed to file is a List of Countries
			if (filename.equals(COUNTRIES_FILENAME)
					|| filename.equals(SELECTED_COUNTRIES_FILENAME))
			{
				// Iterates through the List of countries
				for (E country : data)
				{
					// Places the country's data to a temporary String object
					temp += ((Country) country).getId() + SEPARATOR;
					temp += ((Country) country).getName() + SEPARATOR;
					temp += ((Country) country).getIso2Code() + SEPARATOR;
					temp += ((Country) country).getCapitalCity() + SEPARATOR;
					temp += ((Country) country).getLatitude() + SEPARATOR;
					temp += ((Country) country).getLongitude() + NEWLINE;
				}
			}
			// If data being parsed to file is a List of categories
			else if (filename.equals(CATEGORIES_FILENAME))
			{
				// Iterates through the List of categories
				for (E category : data)
					temp += category.toString() + NEWLINE;
			}
			// If data being parsed to file is the current move status
			else
				temp += data.get(0).toString() + NEWLINE;

			// Writes the data to the file
			bos.write(temp.getBytes());
			Log.i("CachingEngine", "Written " + data.size()
					+ " entries to file.");

			// Flushes and closes the OutputStream object
			bos.flush();
			bos.close();

			return true;
		}

		// If the system encountered a problem whilst creating the Working
		// Directory or whilst writing the data to the file
		catch (IOException ex)
		{
			Log.e("CachingEngine", "Not Writing to File");
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * Parses the contents of a file with the specified filename in the device's
	 * external storage to data.
	 * 
	 * @param filename
	 *            : One of the four contants representing the filename.
	 * @return A List containing the parsed data from the file.
	 * @see #CATEGORIES_FILENAME
	 * @see #COUNTRIES_FILENAME
	 * @see #CURRENT_MOVE_STATUS_FILENAME
	 * @see #SELECTED_COUNTRIES_FILENAME
	 */
	@SuppressWarnings("unchecked")
	private static <E> List<E> readFromFile(String filename)
	{
		// Creates and initialises a File object to store the countries data
		File cacheFile = new File(WORKING_DIRECTORY + filename);

		if (cacheFile.exists())
		{
			// Tries to read the countries data from the cache file
			try
			{
				// Prepares the objects to read the contents of cacheFile
				List<E> data = new ArrayList<E>();
				FileInputStream is = new FileInputStream(cacheFile);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));

				// Gets the "expiry date" of the cache file
				Long expiryDateMillis = Long.parseLong(reader.readLine());

				// If the cached data is NOT out of date
				if (System.currentTimeMillis() <= expiryDateMillis)
				{
					String line;

					// If data being parsed is a List of Countries
					if (filename.equals(COUNTRIES_FILENAME)
							|| filename.equals(SELECTED_COUNTRIES_FILENAME))
					{
						// Iterates through the file creating Country objects
						while ((line = reader.readLine()) != null)
							data.add((E) new Country(line));
					}
					// If data is either list of categories or move status
					else
					{
						// Iterates through the file creating categories
						while ((line = reader.readLine()) != null)
							data.add((E) new String(line));
					}

					// Adds an entry to LogCat describing the current status
					Log.i("CacheEngine", "Read " + data.size()
							+ " entries from cache.");
				}
				// If the cached data is out of date
				else
				{
					// Adds an entry to LogCat describing the current status
					Log.w("CachingEngine",
							"Cached file out of date, refreshing...");
				}

				// Closes the BufferedReader and InputStream objects
				reader.close();
				is.close();

				return data;
			}
			// If the system encountered a problem whilst opening the cache file
			// or whilst reading the file
			catch (IOException ex)
			{
				Log.e("CachingEngine", "Unable to read data from cache.");
				return new ArrayList<E>();
			}
		}
		return new ArrayList<E>();
	}
}
