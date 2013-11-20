package com.worldly.data_models;

/**
 * This class represents an indicator, that hasn't been loaded yet.
 * It simply contains the code for loading and a human-readable description.
 * 
 * @author Annie the Eagle & Team
 * 
 */
public class UnloadedIndicatorDescription {
	private String name;
	private String code;
	
	public UnloadedIndicatorDescription(String n, String c) {
		this.name = n;
		this.code = c;
	}

	public String getName() { return name; }

	public void setName(String name) { this.name = name; }

	public String getCode() { return code; }

	public void setCode(String code) { this.code = code; }
}
