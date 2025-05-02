package com.strive.iti;

import java.time.LocalDateTime;



import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.strive.iti.UploadItiData.UploadStatus;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@EntityListeners(AuditingEntityListener.class)
public class UploadItiDataDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String itiName;
//    private String address;
    private String state;
//    private String principalAndVicePrincipal;
//    private String itiEmailId;
    private String instructorName;
//    private String principalContactNumber;
    private String instructorEmailId;
    private String itiTrade;
    private String district;
    private int noOfTrainees; 		
    private int noOfSeatsAvailable;
	private String courseDurationYear; 						
	private String instructorContactNumber;
    private String remarks;
    private String employmentType;
    
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


    @Enumerated(EnumType.STRING)
    private UploadStatus status;

  
}
