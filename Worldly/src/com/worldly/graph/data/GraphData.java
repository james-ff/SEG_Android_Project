package com.worldly.graph.data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.worldly.graph.exception.CannotBeNullException;
import com.worldly.graph.exception.GraphDataSizeMismatchException;

/**
 * Class that contains the source data for any graph.
 * 
 * Class implements {@link Serializable} therefore, it can be
 * passed between Activities in Bundle -> bundle.putSerializable(GRAPH_DATA_KEY, data);.
 * 
 * See also {@link GraphDataColumn} and {@link GraphDataRow}
 * for more information on how to use this class. 
 * 
 * @author Marek Matejka
 *
 */
public class GraphData implements Serializable
{	
	/**
	 * Default serial version ID.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Key to be used when passing GraphData object between activities.
	 */
	public static final String GRAPH_DATA_KEY = "GraphDataKey";
	
	/**
	 * List of GraphDataRows which contains all the data for the graph.
	 */
	List<GraphDataRow> data;
	
	/**
	 * Constructor which sets rowNames and first line of data.
	 * See also {@link GraphDataRow} and {@link GraphDataSizeMismatchException}.
	 * 
	 * @param rowNames Names of all columns.
	 * @param rowData Data for all columns.
	 * @throws GraphDataSizeMismatchException If sizes do not match 
	 * exception is thrown.
	 */
	public GraphData(GraphDataRow rowNames, GraphDataRow rowData) throws GraphDataSizeMismatchException
	{
		if (rowNames.size() != rowData.size())
			throw new GraphDataSizeMismatchException(rowData.size(), rowNames.size());
		
		data = new LinkedList<GraphDataRow>();
		data.add(rowNames);
		data.add(rowData);
	}
	
	/**
	 * Constructor which adds one column to the data.
	 * See also {@link GraphDataColumn}.
	 * 
	 * @param names Column of row names to be added.
	 * @param values Column of values to be added.
	 * @throws CannotBeNullException If any data from the column is NULL, exception is thrown.
	 */
	public GraphData(GraphDataColumn names, GraphDataColumn values) throws CannotBeNullException
	{
		data = new LinkedList<GraphDataRow>();
		try{
			addColumn(names);
			addColumn(values);
		}catch (GraphDataSizeMismatchException e){e.printStackTrace();} // this will never happen
	}
	
	/**
	 * Constructor which is created using List
	 * of {@link GraphDataRow}.
	 * 
	 * @param data Sets the given List as the source data.
	 * @throws GraphDataSizeMismatchException If any of the rows does not have the
	 * same length as the first row or the data does not contain at least 2 rows
	 * and 2 columns exception is thrown.
	 */
	public GraphData(List<GraphDataRow> data) throws GraphDataSizeMismatchException
	{
		if (data.size() < 2)
			throw new GraphDataSizeMismatchException(data.size(), 2);
		
		int size = data.get(0).size();
		
		if (size < 2)
			throw new GraphDataSizeMismatchException(size, 2);
			
		for (int i = 1; i < data.size(); i++)
			if (data.get(i).size() != size)
				throw new GraphDataSizeMismatchException(data.get(i).size(), size);
		
		this.data = data;
	}
	
	/**
	 * Adds a single row of data.
	 * See also {@link GraphDataRow} and {@link GraphDataSizeMismatchException}.
	 * 
	 * @param rowData Data row to be added.
	 * @throws GraphDataSizeMismatchException If the size of the row
	 * does not match the number of columns exception is thrown.
	 */
	public void addRow(GraphDataRow rowData) throws GraphDataSizeMismatchException
	{
		if (getNumberOfColumns() != rowData.size())
			throw new GraphDataSizeMismatchException(rowData.size(), getNumberOfColumns());
		
		data.add(rowData);
	}
	
	/**
	 * Adds a single column to the data.
	 * See also {@link GraphDataColumn} and {@link GraphDataSizeMismatchException}.
	 * 
	 * @param column Column to be added.
	 * @throws GraphDataSizeMismatchException If the column contains more
	 * or less rows than the current data then the exception is thrown.
	 * @throws CannotBeNullException If any object from the column is NULL, exception is thrown.
	 */
	public void addColumn(GraphDataColumn column) throws GraphDataSizeMismatchException, CannotBeNullException
	{
		if (!data.isEmpty())
		{
			if (data.size() != column.size())
				throw new GraphDataSizeMismatchException(column.size(), data.size());
			
			List<Object> columnData = column.getColumnData();
			for (int i = 0; i < data.size(); i++)
				data.get(i).addRowData(columnData.get(i));
		}
		else
		{
			List<Object> columnData = column.getColumnData();
			
			data.add(new GraphDataRow("", columnData.get(0).toString(), true));
			
			for (int i = 1; i < columnData.size(); i++)
				data.add(new GraphDataRow("", columnData.get(i).toString(), false));
		}
	}
	
	/**
	 * Returns data formatted directly for use with graphs.
	 * 
	 * @return Graph formatted data.
	 */
	public String getData()
	{
		String result = "[";
		for (int i = 0; i < data.size(); i++)
			result += data.get(i).toString()+",";
			
		//replace the last comma (',') for right square bracket (']')
		return result.substring(0,  result.length()-1)+"]";
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return getData();
	}
	
	/**
	 * Returns the number of Columns in the Data.
	 * 
	 * @return Number of columns.
	 */
	public int getNumberOfColumns()
	{
		return data.get(0).size();
	}
	
	/**
	 * Returns the number of Rows in the Data.
	 * 
	 * @return Number of Rows.
	 */
	public int getNumberOfRows()
	{
		return data.size();
	}
	
	/**
	 * Removes a row at a given index from data.
	 * 
	 * <i>Note: Data must always contain at least two rows.
	 * Row of Names and 1 row of Values.</i>
	 * 
	 * @param index Index of a row to be removed.
	 */
	public void removeRow(int index)
	{
		if (getNumberOfRows() == 2 && index <= 1)
			return;
		
		if (index >= 1 && index < getNumberOfRows())
			data.remove(index);
	}
	
	/**
	 * Removes a column at a given index from data.
	 * 
	 * <i>Note: Data must always contain at least 2 columns.
	 * Column with row names and one column with values.</i>
	 * 
	 * @param index Index of a row to be removed.
	 */
	public void removeColumn(int index)
	{
		if (getNumberOfColumns() == 2 && index <= 1)
			return;
				
		if (index >= 1 && index < getNumberOfColumns())
			for (int i = 0; i < data.size(); i++)
				data.get(i).removeRowData(index);
	}
	
	/**
	 * Returns index of column with the name equal to <i>name</i>.
	 * 
	 * @param name Name of the column.
	 * @return Index of the column or -1 if nothing found.
	 */
	public int getIndexOfColumnByName(String name)
	{
		List<Object> names = data.get(0).getData();
		for (int i = 0; i < names.size(); i++)
			if (names.get(i).equals(name))
				return i;
		return -1;
	}
}
