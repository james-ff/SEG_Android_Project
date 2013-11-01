package com.worldly.data_models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class represents an indicator, a data value for an arbitrary country,
 * it represents a single property for a country. It contains the building blocks
 * to define an indicator and would be used in a K,V pair of type <Indicator, Value>.
 * 
 * @author Annie the Eagle & Team
 * 
 */
public class Indicator {
	private final String ID = "id", READABLE_NAME = "name", SOURCE_KEY = "source", SOURCE_NAME = "value";
	
	private String id;
	private String name;
	private String sourceName;
	private int sourceID;
	
	/**
	 * Constructs a new Indicator object with the specified JSON raw data.
	 * 
	 * @param rawData: A JSONObject containing raw data about an Indicator.
	 */
	public Indicator(JSONArray rawArray) {
		super();
				
		try { // Attempts to parse rawData
			JSONObject rawData = rawArray.getJSONArray(1).getJSONObject(0);
			this.name = rawData.getString(READABLE_NAME);
			this.id = rawData.getString(ID);
			this.sourceName = rawData.getJSONObject(SOURCE_KEY).getString(SOURCE_NAME);
			this.sourceID = Integer.parseInt(rawData.getJSONObject(SOURCE_KEY).getString(ID));
			
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public String getId() { return id; }
	public void setId(String id) { this.id = id; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getSourceName() { return sourceName; }
	public void setSourceName(String sourceName) { this.sourceName = sourceName; }

	public int getSourceID() { return sourceID; }
	public void setSourceID(int sourceID) { this.sourceID = sourceID; }
}