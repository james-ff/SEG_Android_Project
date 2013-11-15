package com.worldly.graph.data;

import java.util.LinkedList;
import java.util.List;

import com.worldly.graph.exception.CannotBeNullException;

/**
 * Class representing a row of data used as a data source for graphs.
 * Row contains a name, which cannot be NULL.
 * 
 * @author Marek Matejka
 *
 */
public class GraphDataRow 
{
	/**
	 * First element is the name of the row and the rest is data.
	 */
	List<Object> rowData;
	boolean namesRow = false;
	
	/**
	 * Constructor which directly sets the given List as
	 * the rowData. The first element in the List is considered
	 * to be the name of the row.
	 * 
	 * @param rowData List of Objects to be stored in a given row.
	 * @param namesRow Specifies if the row is the names (first) row.
	 */
	public GraphDataRow(List<Object> rowData, boolean namesRow)
	{
		this.rowData = rowData;
		this.namesRow = namesRow;
	}
	
	/**
	 * Constructor which adds a name and a single data to the List.
	 * 
	 * @param name Name of the row.
	 * @param data Data to be stored in the row.
	 * @param namesRow Specifies if the row is the names (first) row.
	 * @throws CannotBeNullException Thrown when name or data is NULL.
	 */
	public GraphDataRow(String name, Object data, boolean namesRow) throws CannotBeNullException
	{
		if (name == null || name.equals(null) || data == null || data.equals(null))
			throw new CannotBeNullException();
		
		rowData = new LinkedList<Object>();
		this.rowData.add(name);
		this.rowData.add(data);
		
		this.namesRow = namesRow;
	}
	
	/**
	 * Adds another data Object to the row.
	 * 
	 * @param data Data Object to be added to the row.
	 * @throws CannotBeNullException If data is NULL exception is thrown.
	 */
	public void addRowData(Object data) throws CannotBeNullException
	{
		if (data == null || data.equals(null))
			throw new CannotBeNullException();
		this.rowData.add(data);
	}
	
	/**
	 * Removes data from the row from a given index.
	 * 
	 * @param index Index of value to be removed.
	 */
	public void removeRowData(int index)
	{
		this.rowData.remove(index);
	}
	
	/**
	 * Returns data from the row as a formatted String.
	 * 
	 * @return Formatted data from the row.
	 */
	public String getRowData()
	{
		if (namesRow)
			return getNamesRowData();
		
		String result = "[";
		
		//get row name
		result += "'"+rowData.get(0).toString()+"',";
		
		//get row values
		for (int i = 1; i < rowData.size(); i++)
			result += rowData.get(i).toString()+",";
		
		//replace the last comma (',') with right square bracket (']')
		return result.substring(0, result.length()-1) + "]";
	}
	
	/**
	 * Returns data from the names (first) row as a formatted String
	 * ready to be used with graph source data.
	 * 
	 * @return Formatted data from the names (first) row.
	 */
	private String getNamesRowData()
	{
		String result = "[";
				
		for (int i = 0; i < rowData.size(); i++)
			result += "'"+rowData.get(i).toString()+"',";
		
		result = result.substring(0, result.length()-1);
		
		return result + "]";
	}
	
	/**
	 * Returns the data stored in the row in an unchanged format.
	 * 
	 * @return Data from the row in an unchanged format.
	 */
	public List<Object> getData()
	{
		return rowData;
	}
	
	/**
	 * Returns the number of elements in the row.
	 * 
	 * @return Number of elements in the row.
	 */
	public int size()
	{
		return rowData.size();
	}
	
	/**
	 * Returns the name of the row.
	 * 
	 * @return Name of the row.
	 */
	public String getRowName()
	{
		return rowData.get(0).toString();
	}
	
	/**
	 * Sets the name of the row.
	 * 
	 * @param name Name of the row.
	 */
	public void setRowName(String name)
	{
		rowData.set(0, name);
	}
	
	/**
	 * Specifies whether the row is used as a names (first) row.
	 * 
	 * @return TRUE if it is names (first) row, FALSE otherwise.
	 */
	public boolean isNamesRow()
	{
		return namesRow;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return getRowData();
	}
}
