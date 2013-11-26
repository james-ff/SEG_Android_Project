package com.worldly.graph.data;

import java.util.LinkedList;
import java.util.List;

import com.worldly.graph.exception.CannotBeNullException;

/**
 * Class representing a column of data used as a data source for graphs.
 * 
 * @author Marek Matejka
 *
 */
public class GraphDataColumn 
{
	/**
	 * Starts with the name of the column and follows with data.
	 */
	private List<Object> columnData;
	
	/**
	 * Constructor which directly sets the given List as
	 * the columnData.
	 * 
	 * @param columnData List of Objects to be stored in a given column.
	 */
	public GraphDataColumn(List<Object> columnData)
	{
		this.columnData = columnData;
	}
	
	/**
	 * Constructor which adds a single Object to the List.
	 * 
	 * @param name Name of the column.
	 * @param data Data to be stored in the column.
	 * @throws CannotBeNullException If name or data is NULL, exception is thrown.
	 */
	public GraphDataColumn(Object name, Object data) throws CannotBeNullException
	{
		if (name == null || name.equals(null)|| data == null || data.equals(null))
			throw new CannotBeNullException();
		
		columnData = new LinkedList<Object>();
		this.columnData.add(name);
		this.columnData.add(data);
	}
	
	/**
	 * Adds another data Object to the column.
	 * 
	 * @param data Data Object to be added to the column.
	 * @throws CannotBeNullException If data is NULL, exception is thrown.
	 */
	public void addColumnData(Object data) throws CannotBeNullException
	{
		if (data == null || data.equals(null))
			throw new CannotBeNullException();
		
		this.columnData.add(data);
	}
	
	/**
	 * Removes data from a given index from the column.
	 * 
	 * @param index Index of an element to be removed.
	 */
	void removeColumnData(int index)
	{
		if (index >= 0 && index < columnData.size())
			this.columnData.remove(index);
	}
	
	/**
	 * Returns the data stored in the column in an unchanged format.
	 * 
	 * @return Data from the column in an unchanged format.
	 */
	List<Object> getColumnData()
	{
		return columnData;
	}
	
	/**
	 * Returns the number of elements in the column.
	 * 
	 * @return Number of elements in the column.
	 */
	int size()
	{
		return columnData.size();
	}
	
	/**
	 * Returns data from the column in a human readable format.
	 * 
	 * @return Column data in a human readable format.
	 */
	private String getColumnDataString()
	{
		String result = "";
		
		for (int i = 0; i < columnData.size(); i++)
			result += columnData.get(i).toString()+", ";
			
		return result.substring(0, result.length()-1);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{		
		return getColumnDataString();
	}
	
	/**
	 * Returns the name of the column.
	 * 
	 * @return Column name.
	 */
	String getColumnName()
	{
		return columnData.get(0).toString();
	}
	
	/**
	 * Sets the name of the column.
	 * Replaces whatever is at position 0 for the given name!
	 * 
	 * @param name Name of the column.
	 * @throws CannotBeNullException If name is NULL, exception is thrown.
	 */
	void setColumnName(String name) throws CannotBeNullException
	{
		if (name == null || name.equals(null))
			throw new CannotBeNullException();
		
		columnData.set(0, name);
	}
}
