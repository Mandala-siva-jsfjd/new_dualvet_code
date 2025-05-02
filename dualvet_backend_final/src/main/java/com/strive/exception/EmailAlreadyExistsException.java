package com.strive.exception;

public class EmailAlreadyExistsException extends RuntimeException
{

	private String message;
	public EmailAlreadyExistsException(String message)
	{
		super(message);
		this.message = message;
	}
	public EmailAlreadyExistsException()
	{
		super("EMAIL ALREADY EXISTS..");
	}
	
}
