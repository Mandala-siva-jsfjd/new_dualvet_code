package com.strive.reportIndustry;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndustryReportDto 
{
	
	private Long sno;
    private String industryName;
    private String contactName;
    private String contactNumber;
    private String emailId;
    private String nearestCity;
    private String district; 
    private String state;    
    private Integer pincode;   
    private Double lat;
    private Double lon;
    private String  designationOfSpoc;
    private String sizeOfIndustry;
    private String sector; 
//    private String domain;
    private String ojtSupport;
    private String noOfOjtTrainees;
    private String apprenticeshipSupport;
    private String noOfApprenticeship;
    private String placementsSupport;
    private String noOfPlacements;
    private String isStipendProvided; 
    private String isTransportProvided;
    private String isFoodProvided;
    private String isAccommodationProvided; 
    private String isMouSign;
    private String academicYear;

}
