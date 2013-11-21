package com.worldly.data_models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * This class represents an indicator, a data value for an arbitrary country,
 * it represents a single property for a country. It contains the building blocks
 * to define an indicator and would be used in a K,V pair of type <Indicator, Value>.
 * 
 * @author Annie the Eagle & Team
 * 
 */
public class Indicator {
	
	/**
	 * Constant representing the key to obtain the respective value from the
	 * JSON raw data.
	 */
	private final String ID = "id", READABLE_NAME = "name", SOURCE_KEY = "source", SOURCE_NAME = "value";
	
	private String id;
	private String name;
	private String sourceName;
	private String category;
	private int sourceID;
	
	/**
	 * Constructs a new Indicator object with the specified JSON raw data.
	 * 
	 * @param rawArray: A JSONArray containing raw data about an Indicator.
	 */
	public Indicator(String category, JSONArray rawArray) {
		super();
			
		Log.e("DEBUG", rawArray.toString());
		
		this.setCategory(category); // Sets a category that we can use to retrieve from for each specific subheading / different type of activity
		
		try { // Attempts to parse rawData
			JSONObject rawData = rawArray.getJSONArray(1).getJSONObject(0);
			this.name = rawData.getString(READABLE_NAME);
			this.id = rawData.getString(ID);
			this.sourceName = rawData.getJSONObject(SOURCE_KEY).getString(SOURCE_NAME);
			this.sourceID = Integer.parseInt(rawData.getJSONObject(SOURCE_KEY).getString(ID));		
		} 
		catch (JSONException e) {
			this.setCategory("broken");
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves the ID of the Indicator object.
	 * 
	 * @return A String containing the ID of the Indicator object.
	 */
	public String getId() { return id; }
	
	/**
	 * Assigns an ID to the Indicator object.
	 * 
	 * @param sourceName
	 *            : A String containing the ID of the Indicator object.
	 */
	public void setId(String id) { this.id = id; }

	/**
	 * Retrieves the name of the Indicator object.
	 * 
	 * @return A String containing the name of the Indicator object.
	 */
	public String getName() { return name; }
	
	/**
	 * Assigns a name to the Indicator object.
	 * 
	 * @param name
	 *            : A String containing the name of the Indicator object.
	 */
	public void setName(String name) { this.name = name; }

	/**
	 * Retrieves the source name of the Indicator object.
	 * 
	 * @return A String containing the source name of the Indicator object.
	 */
	public String getSourceName() { return sourceName; }
	
	/**
	 * Assigns a source name to the Indicator object.
	 * 
	 * @param sourceName
	 *            : A String containing the source name of the Indicator object.
	 */
	public void setSourceName(String sourceName) { this.sourceName = sourceName; }

	/**
	 * Retrieves the source ID of the Indicator object.
	 * 
	 * @return An int containing the source ID of the Indicator object.
	 */
	public int getSourceID() { return sourceID; }
	
	/**
	 * Assigns a source ID to the Indicator object.
	 * 
	 * @param sourceID
	 *            : An int containing the source ID of the Indicator object.
	 */
	public void setSourceID(int sourceID) { this.sourceID = sourceID; }

	/**
	 * Retrieves the category of the Indicator object.
	 * 
	 * @return A String containing the category of the Indicator object.
	 */
	public String getCategory() { return category; }

	/**
	 * Assigns a category to the Indicator object.
	 * 
	 * @param sourceID: A String containing the category of the Indicator object.
	 */
	public void setCategory(String category) { this.category = category; }
}