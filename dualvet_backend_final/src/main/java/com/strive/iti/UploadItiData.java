package com.strive.iti;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.strive.iti.master.ItiInstructor;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
@Entity
@Table(name = "iti_staging")
@EntityListeners(AuditingEntityListener.class)
public class UploadItiData {

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

    @Column(name = "meta_data_reg_id", insertable = false, updatable = false)
    private int regid;

    public enum UploadStatus {
        Pending, Fail, Complete, Loading
    }

    @Enumerated(EnumType.STRING)
    private UploadStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meta_data_reg_id")
    private UploadMetaDataIti metaData;

//    @OneToMany(mappedBy = "uploadItiData", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @ToString.Exclude
//    private List<ItiInstructor> itiInstructors;

    // Default constructor
    public UploadItiData() {
        this.status = UploadStatus.Pending;
    }
}
