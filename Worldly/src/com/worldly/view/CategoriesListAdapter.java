package com.worldly.view;

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
 * @author Rafael da Silva Costa & Team TODO comments
 */
public class CategoriesListAdapter extends BaseExpandableListAdapter
{

	private Context context;
	private List<String> groups;
	private Map<String, List<String>> childs;

	public CategoriesListAdapter(Context context, List<String> groups,
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

		if (convertView == null)
		{
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_child, null);
		}

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

		if (convertView == null)
		{
			LayoutInflater inflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_group, null);
		}

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
