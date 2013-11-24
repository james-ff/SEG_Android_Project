package com.worldly.custom_adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SpinnerAdapter extends ArrayAdapter<String>
{
	Context context;
	List<String> categories;
	
	public SpinnerAdapter(Context context, int resource, List<String> categories) 
	{
		super(context, resource, categories);
		this.categories = categories;
		this.context = context;
	}
	
    @Override
    public View getView(int position, View convertView, ViewGroup parent) 
    {
        TextView label = new TextView(context);
        label.setText(categories.get(position));
        return label;
    }
	
}
