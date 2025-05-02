package com.strive.exception;

public enum ErrorMessages {
	MISSING_REQUIRED_FILED("Missig required fileds, pls check documentation"),
	RECORD_ALREDAY_EXISTS("This data alreday present in the system"),
	AADHAR_ALREDAY_EXISTS("This Aadhar number alreday present in the system"),
	EMAIL_ADDRESS_NOT_VERIFIED("Email Address could not be verified"),
	RECORD_NOT_FOUND("No data found with the guven id");
	
	private ErrorMessages(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	
	private String errorMessage ;

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	

}