package com.strive.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(HttpStatus.OK)
public class UserNotCreatedException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4549741980138850968L;

	/**
	 * 
	 */



	public UserNotCreatedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}