package com.strive.itiReport;

import lombok.Data;

@Data
public class ItiDetailsDTO {
	
	private long sno;
	private String state;
	private String district;
    private String itiName;
    private String itiEmailId;
    private String principalAndVicePrincipal;
    private String instructorName;
    private String instructorEmailId;
    private String trade;
    private int noOfTrainees;
    private int noOfSeatsAvailable;

    // Constructor
    public ItiDetailsDTO( long sno,String state,String district,String itiName, String itiEmailId, String principalAndVicePrincipal, String instructorName, String instructorEmailId, String trade, int noOfTrainees, int noOfSeatsAvailable) {
        this.sno = sno;
        this.state = state;
        this.district = district;
    	this.itiName = itiName;
        this.itiEmailId = itiEmailId;
        this.principalAndVicePrincipal = principalAndVicePrincipal;
        this.instructorName = instructorName;
        this.instructorEmailId = instructorEmailId;
        this.trade = trade;
        this.noOfTrainees = noOfTrainees;
        this.noOfSeatsAvailable = noOfSeatsAvailable;
    }
    
}