package com.worldly.controller;

import java.util.ArrayList;
import java.util.List;

import com.worldly.data_models.Country;
import com.worldly.data_store.CachingEngine;
import com.worldly.data_store.ListOfIndicators;

/**
 * This class acts as a global data controller, that stores the decisions made
 * at each decision making activity. It supports saving and restoring it's state
 * to/from file.
 * 
 * Current Decisions supported: - Choosing Move Type - Choosing Countries to
 * compare
 * 
 * @author James Bellamy & Rafael da Silva Costa & Team
 * 
 */
public class WorldlyController
{

	private static WorldlyController instance = null;

	/**
	 * Constant representing the current move status selected by the user.
	 */
	public static final int FAMILY_MOVE = 0, PERSONAL_MOVE = 1,
			BUSINESS_MOVE = 2, CUSTOM_MOVE = 3;

	private int currentMoveStatus;
	private int currentlySelectedCountryIndex;
	private List<Country> currentSelectedCountries;
	private List<String> categories;

	private WorldlyController()
	{
		super();
		loadState();
	}

	public static WorldlyController getInstance()
	{
		if (instance == null)
		{
			instance = new WorldlyController();
		}
		return instance;
	}

	/**
	 * Loads a previous state of this object from a cached file.
	 */
	public void loadState()
	{
		this.currentMoveStatus = CachingEngine.getCachedCurrentMoveStatus();
		this.currentSelectedCountries = CachingEngine
				.getCachedSelectedCountries();
		this.categories = CachingEngine.getCachedCategories();
	}

	/**
	 * Saves the current state of this object to cache.
	 */
	public void saveState()
	{
		CachingEngine.writeCurrentMoveStatusCache(getCurrentMoveStatus());
		CachingEngine
				.writeSelectedCountriesCache(getCurrentSelectedCountries());
		CachingEngine.writeCategoriesCache(getCategories());
	}

	/**
	 * Gets the current move status.
	 * 
	 * @return One of the four constants representing the current move status.
	 * @see #BUSINESS_MOVE
	 * @see #CUSTOM_MOVE
	 * @see #FAMILY_MOVE
	 * @see #PERSONAL_MOVE
	 */
	public int getCurrentMoveStatus()
	{
		return this.currentMoveStatus;
	}

	/**
	 * Sets the current move status.
	 * 
	 * @param currentMoveStatus
	 *            : One of the four constants representing the new move status.
	 * @param customCategories
	 *            : A list containing the custom indicators to be used by the
	 *            application.
	 * @see #BUSINESS_MOVE
	 * @see #CUSTOM_MOVE
	 * @see #FAMILY_MOVE
	 * @see #PERSONAL_MOVE
	 */
	public void setCurrentMoveStatus(int currentMoveStatus,
			List<String> customCategories)
	{
		this.currentMoveStatus = currentMoveStatus;
		this.categories = new ArrayList<String>();

		switch (currentMoveStatus)
		{
			case BUSINESS_MOVE:
				categories.add(ListOfIndicators.CATEGORY_BUSINESS);
				categories.add(ListOfIndicators.CATEGORY_CITY_LIFE);
				categories.add(ListOfIndicators.CATEGORY_DEMOGRAPHICS);
				categories.add(ListOfIndicators.CATEGORY_EMPLOYMENT_PROSPECTS);
				categories.add(ListOfIndicators.CATEGORY_FINANCE);
				categories.add(ListOfIndicators.CATEGORY_QUALITY_OF_LIFE);
				break;
			case FAMILY_MOVE:
				categories.add(ListOfIndicators.CATEGORY_CITY_LIFE);
				categories.add(ListOfIndicators.CATEGORY_CLIMATE);
				categories.add(ListOfIndicators.CATEGORY_DEMOGRAPHICS);
				categories.add(ListOfIndicators.CATEGORY_EDUCATION);
				categories.add(ListOfIndicators.CATEGORY_QUALITY_OF_LIFE);
				categories.add(ListOfIndicators.CATEGORY_RURAL_LIFE);
				break;
			case PERSONAL_MOVE:
				categories.add(ListOfIndicators.CATEGORY_CITY_LIFE);
				categories.add(ListOfIndicators.CATEGORY_CLIMATE);
				categories.add(ListOfIndicators.CATEGORY_DEMOGRAPHICS);
				categories.add(ListOfIndicators.CATEGORY_EMPLOYMENT_PROSPECTS);
				categories.add(ListOfIndicators.CATEGORY_QUALITY_OF_LIFE);
				categories.add(ListOfIndicators.CATEGORY_RURAL_LIFE);
				break;
			case CUSTOM_MOVE:
				categories.addAll(customCategories);
				break;
		}
	}

	/**
	 * Gets the List of the categories currently selected.
	 */
	public List<String> getCategories()
	{
		return this.categories;
	}

	/**
	 * Gets the current selected countries.
	 * 
	 * @return : A List containing the selected Country objects.
	 */
	public List<Country> getCurrentSelectedCountries()
	{
		return this.currentSelectedCountries;
	}

	/**
	 * Sets the current selected countries.
	 * 
	 * @param currentSelectedCountries
	 *            : A List containing the selected Country objects.
	 */
	public void setCurrentSelectedCountries(
			List<Country> currentSelectedCountries)
	{
		this.currentSelectedCountries = currentSelectedCountries;
	}
	
	public int getCurrentlySelectedCountryIndex() {
		return currentlySelectedCountryIndex;
	}

	public void setCurrentlySelectedCountryIndex(int currentlySelectedCountryIndex) {
		this.currentlySelectedCountryIndex = currentlySelectedCountryIndex;
	}

	public Country getCurrentlySelectedCountry() {
		return getCurrentSelectedCountries().get(getCurrentlySelectedCountryIndex());
	}
	
	public void incrementSelectedCountryIndex() {
		currentlySelectedCountryIndex++;
	}
	
	public void decrementSelectedCountryIndex() {
		currentlySelectedCountryIndex--;
	}
}
