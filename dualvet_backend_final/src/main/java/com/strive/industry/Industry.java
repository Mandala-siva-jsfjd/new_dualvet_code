package com.strive.industry;

import java.time.LocalDateTime;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@ToString(exclude = "metaData")
@EntityListeners(AuditingEntityListener.class)
@Table(name = "industry_staging")
public class Industry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
	 @Column(updatable = false)
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

    @Column(name = "meta_data_reg_id", insertable = false, updatable = false)
    private int regid; // Mapping to the meta_data_reg_id column

    @ManyToOne
    private IndustryMetaData metaData;

    public enum UploadStatus 
    {
        Pending, Fail, Complete, Loading
    }

    public Industry() {
        this.status = UploadStatus.Pending;
    }
}
