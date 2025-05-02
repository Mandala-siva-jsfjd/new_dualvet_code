package com.strive.ipt;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.strive.ipt.InPlantTraining.UploadStatus;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@EntityListeners(AuditingEntityListener.class)
public class InPlantTrainingDto 
{
    private int id;
    private String traineesName;
    private String traineesCollegeRegId;
    private String traineesItiTrade;
    private String phoneNumber;
    private String emailId;
    private long stipend;
    private String itiName;
    private String instructorName;
    private int iptYear;    
    private String industryName; // New field for industry name
    private String insuranceStatus;
    private String ppeKitStatus;
    private LocalDate iptStart;
    private LocalDate iptEnd;
    private String iptDiaryProvided;
    private String remarks;
    private String academicYear;
    private String employmentType;

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

    @Enumerated(EnumType.STRING)
    private UploadStatus status;
}
