package com.worldly.custom_adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

public class CompareExpandableListAdapter extends BaseExpandableListAdapter {

	public CompareExpandableListAdapter() {
		
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		
		return null;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		
		return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		
		return null;
	}

	@Override
	public int getGroupCount() {
		
		return 0;
	}

	@Override
	public long getGroupId(int groupPosition) {
		
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		
		return null;
	}

	@Override
	public boolean hasStableIds() {
		
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		
		return false;
	}

}
