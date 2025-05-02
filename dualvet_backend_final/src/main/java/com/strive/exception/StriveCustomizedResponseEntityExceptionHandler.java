package com.strive.exception;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class StriveCustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler{

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) throws Exception {
		
		ExptionResponse exptionResponse = 
					new ExptionResponse(new Date() , ex.getMessage(), request.getDescription(false));
		
		return new ResponseEntity<Object>(exptionResponse,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	@ExceptionHandler(UserNotFoundException.class)
	public final ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) throws Exception {
		
		ExptionResponse exptionResponse = 
					new ExptionResponse(new Date() , ex.getMessage(), request.getDescription(false));
		
		return new ResponseEntity<Object>(exptionResponse,HttpStatus.NOT_FOUND);
	} 
	
	
	
	
	protected ResponseEntity<Object> handleMethodArResponseEntity(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

		ExptionResponse exptionResponse = 
				new ExptionResponse(new Date() , "User Not Created", ex.getBindingResult().toString());
	
	return new ResponseEntity<Object>(exptionResponse,HttpStatus.BAD_REQUEST);
		
	}

}