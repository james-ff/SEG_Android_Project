package com.worldly.adapter_subclass;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.worldly.data_models.Country;

public class CountryArrayAdapter extends ArrayAdapter<Country> {

	// Custom Object values for the spinner (Country)
    private ArrayList<Country> values;
    private Context context;

    public CountryArrayAdapter(Context context, int textViewResourceId, ArrayList<Country> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
        
        this.setNotifyOnChange(true);
    }

    public int getCount(){
       return values.size();
    }

    public Country getItem(int position){
       return values.get(position);
    }

    public long getItemId(int position){
       return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(values.get(position).getIso2Code() + " - " + values.get(position).getName());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(values.get(position).getIso2Code() + " - " + values.get(position).getName());
    	return label;
    }
}