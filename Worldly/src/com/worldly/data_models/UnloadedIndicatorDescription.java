package com.worldly.data_models;

/**
 * This class represents an indicator, that hasn't been loaded yet.
 * It simply contains the code for loading and a human-readable description.
 * 
 * @author Ferdinand Keller & Team
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

	public String getCode() { return code; }
}
