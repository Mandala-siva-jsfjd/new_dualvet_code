package com.strive.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException{
	 
		private static final long serialVersionUID = -3838498471137291983L;



		public UserNotFoundException(String message) {
			super(message);
			// TODO Auto-generated constructor stub
		}
}