package com.worldly.graph.exception;

/**
 * Exception thrown each time the size of the data does not fit 
 * the current state.
 * 
 * @author Marek Matejka
 *
 */
public class GraphDataSizeMismatchException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Exception constructor with message.
	 * 
	 * @param received How many elements were received.
	 * @param expected How many elements were expected.
	 */
	public GraphDataSizeMismatchException(int received, int expected)
	{
		super(buildMessage(received, expected));
		
		//UNCOMMENT TO MAKE THE APPLICATION CRASH WHEN THIS PROBLEM IS FOUND
		//throw new RuntimeException(buildMessage(received, expected));
	}
	
	/**
	 * Builds the Exception message based on how many elements were received 
	 * and how many were expected.
	 * 
	 * @param received How many elements were received.
	 * @param expected How many elements were expected.
	 * @return Message based on how many elements were received and how many
	 * were expected.
	 */
	private static String buildMessage(int received, int expected)
	{
		if (received < expected)
			return "Not enough data received! Expected "+expected+", but received only "+received+".";
		else
			return "Too much data received! Expected "+expected+", but received "+received+".";
	}
}
