package com.worldly.data_models;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;

@SuppressLint("UseSparseArrays")
public class IndicatorDataBlock {
	private Map<Integer, Object> data = new HashMap<Integer, Object>();
	private Indicator referencing;
	
	public IndicatorDataBlock(Indicator r) {
		this.referencing = r;
	}
	
	public boolean addDataByYear (int year, Object data) {
		try { this.data.put(year, data); return true; }
		catch (Exception ex) { ex.printStackTrace(); return false; }
	}
	
	public boolean modifyDataInYear(int year, Object newValue) {
		try {
			this.data.remove(year); 
			this.data.put(year, newValue);
			return true;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	public void removeDataByYear(int year) { this.data.remove(year); }
	
	public Object getDataByYear (int year) { return this.data.get(year); }
	
	public Collection<Object> getAllData() { return this.data.values(); }
	
	public Indicator getIndicatorReferenced() { return this.referencing; }
	
	public void setIndicatorReferenced(Indicator i) { this.referencing = i; }
}
