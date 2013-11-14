package com.worldly.graph.data;

import java.util.LinkedList;
import java.util.List;

import com.worldly.graph.exception.CannotBeNullException;
import com.worldly.graph.exception.GraphDataSizeMismatchException;

/**
 * Class that contains the source data for any graph.
 * The Data is organized as a table with rows and columns.
 * See also {@link GraphDataColumn} and {@link GraphDataRow}
 * for more information on how to use this class.
 * 
 * @author Marek Matejka
 *
 */
public class GraphData 
{	
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
	 * @param column Column to be added.
	 * @throws CannotBeNullException If any data from the column is NULL, exception is thrown.
	 */
	public GraphData(GraphDataColumn column) throws CannotBeNullException
	{
		data = new LinkedList<GraphDataRow>();
		try{
			addColumn(column);
		}catch (GraphDataSizeMismatchException e){e.printStackTrace();} // this will never happen
	}
	
	/**
	 * Constructor which is created using List
	 * of {@link GraphDataRow}.
	 * 
	 * @param data Sets the given List as the source data.
	 * @throws GraphDataSizeMismatchException If any of the rows does not have the
	 * same length as the first row exception is thrown.
	 */
	public GraphData(List<GraphDataRow> data) throws GraphDataSizeMismatchException
	{
		int size = data.get(0).size();
		
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
	 * @throws CannotBeNullException If the rowData or any data within the row is NULL.
	 */
	public void addRow(GraphDataRow rowData) throws GraphDataSizeMismatchException, CannotBeNullException
	{
		if (rowData == null || rowData.containsNull())
			throw new CannotBeNullException();
		
		if (getNamesSize() != rowData.size())
			throw new GraphDataSizeMismatchException(rowData.size(), getNamesSize());
				
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
		if (column == null)
			throw new CannotBeNullException();
		
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
			
		result = result.substring(0,  result.length()-1);
		
		return result+"]";
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
	 * Returns the size of the first row.
	 * 
	 * @return Size of the first row.
	 */
	private int getNamesSize()
	{
		return data.get(0).size();
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
}
