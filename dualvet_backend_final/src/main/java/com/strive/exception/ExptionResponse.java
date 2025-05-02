package com.strive.exception;
import java.util.Date;

public class ExptionResponse {
	private Date timeStamp;
	private String excpetionMessage;
	private String exceptionDetails;
	
	
	public ExptionResponse(Date timeStamp, String excpetionMessage, String exceptionDetails) {
		super();
		this.timeStamp = timeStamp;
		this.excpetionMessage = excpetionMessage;
		this.exceptionDetails = exceptionDetails;
	}
	
	
	
	public Date getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getExcpetionMessage() {
		return excpetionMessage;
	}
	public void setExcpetionMessage(String excpetionMessage) {
		this.excpetionMessage = excpetionMessage;
	}
	public String getExceptionDetails() {
		return exceptionDetails;
	}
	public void setExceptionDetails(String exceptionDetails) {
		this.exceptionDetails = exceptionDetails;
	}
	
	
	
	

}