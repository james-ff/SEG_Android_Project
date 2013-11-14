package com.worldly.graph.exception;

/**
 * Exception thrown each time Object is NULL and it should not be.
 * 
 * @author Marek Matejka
 *
 */
public class CannotBeNullException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	private static final String message = "Object cannot be NULL!";
	
	/**
	 * General constructor.
	 */
	public CannotBeNullException()
	{
		super(message);
		
		//UNCOMMENT TO MAKE THE APPLICATION CRASH WHEN THIS PROBLEM IS FOUND
		//throw new RuntimeException(message);
	}
}
