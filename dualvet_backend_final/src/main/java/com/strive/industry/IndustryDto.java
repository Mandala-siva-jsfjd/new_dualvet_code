package com.strive.industry;

import java.time.LocalDateTime;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.strive.industry.Industry.UploadStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;


@Data
@EntityListeners(AuditingEntityListener.class)
public class IndustryDto 
{
	private long id;
    private String industryName;
    private String contactName;
    private String contactNumber;
    private String emailId;
    private String nearestCity;
    private String district;
    private String state;
    private int pincode;
    private double lat;
    private double lan;
    private String designationOfSpoc;
    private String sizeOfIndustry;
    private String sector;
//    private String domain;
    private String trade;
    private String ojtSupport;
    private int noOfOjtTrainees;
    private String apprenticeshipSupport;
    private int noOfApprenticeship;
    private String placementsSupport;
    private int noOfPlacements;
    private String isStipendProvided;
    private String isTransportProvided;
    private String isFoodProvided;
    private String isAccommodationProvided;
    private String isMouSign;   
    private String academicYear;
    @CreatedBy
    @Column
    private int createdBy;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private LocalDateTime createdOn;

    @LastModifiedBy
    private String updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private LocalDateTime updatedOn;

    private String remarks;
    
    @Enumerated(EnumType.STRING)
    private UploadStatus status;
	
}
