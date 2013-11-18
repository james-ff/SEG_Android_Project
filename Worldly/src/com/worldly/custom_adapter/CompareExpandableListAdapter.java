package com.worldly.custom_adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.worldly.R;

/**
 * This class represents the adapter used by the ExpandableList in the
 * CompareCategoriesActivity, it extends BaseExpandableListAdapter and contains
 * implementations for all its inherited methods.
 * 
 * @author Rafael da Silva Costa & Team
 */
public class CompareExpandableListAdapter extends BaseExpandableListAdapter
{
	private Context context;
	private List<String> groups;
	private Map<String, List<String>> childs;

	/**
	 * Constructs the Adapter object with the specified data.
	 * 
	 * @param context
	 *            : The activity's context.
	 * @param groups
	 *            : The group elements of the expandable list.
	 * @param childs
	 *            : The child elements of the expandable list.
	 */
	public CompareExpandableListAdapter(Context context, List<String> groups,
			Map<String, List<String>> childs)
	{
		super();
		this.context = context;
		this.groups = groups;
		this.childs = childs;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition)
	{
		return childs.get(groups.get(groupPosition)).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition)
	{
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent)
	{
		String childTitle = getChild(groupPosition, childPosition).toString();

		// If the old view cannot be reused
		if (convertView == null)
		{
			// Inflates a new child layout placing it on convertView
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_child, null);
		}

		// Creates and initialises a TextView object to display the child title
		TextView tvListChild = (TextView) convertView
				.findViewById(R.id.tvListChild);
		tvListChild.setText(childTitle);

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition)
	{
		return childs.get(groups.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition)
	{
		return groups.get(groupPosition);
	}

	@Override
	public int getGroupCount()
	{
		return groups.size();
	}

	@Override
	public long getGroupId(int groupPosition)
	{
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent)
	{
		String groupTitle = getGroup(groupPosition).toString();
		
		// If the old view cannot be reused
		if (convertView == null)
		{
			// Inflates a new group layout placing it on convertView
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_group, null);
		}

		// Creates and initialises a TextView object to display the group title
		TextView tvGroup = (TextView) convertView.findViewById(R.id.tvGroup);
		tvGroup.setTypeface(null, Typeface.BOLD);
		tvGroup.setText(groupTitle);

		return convertView;
	}

	@Override
	public boolean hasStableIds()
	{
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition)
	{
		return true;
	}
}