package com.strive.ipt;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import com.strive.ipt.master.IptTrainees;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "inplant_training_staging")
public class InPlantTraining {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    
    private String employmentType;

    private long industrialId;

    private String industryName;

    private String insuranceStatus;

    private String ppeKitStatus;

    @DateTimeFormat(pattern="dd/MM/yyyy")
    private LocalDate iptStart;

    @DateTimeFormat(pattern="dd/MM/yyyy")
    private LocalDate iptEnd;

    private String iptDiaryProvided;

    private String remarks;
    
    private String academicYear;

    @CreatedBy
    @Column(updatable = false)
    private int createdBy; 

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdOn; 

    @LastModifiedBy
    private String updatedBy; 

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private LocalDateTime updatedOn; 

    @Enumerated(EnumType.STRING)
    private UploadStatus status;

    public enum UploadStatus {
        Pending, Fail, Complete, Loading
    }

    public InPlantTraining() {
        this.status = UploadStatus.Pending;
    }

    @Column(name = "meta_data_reg_id", insertable = false, updatable = false)
    private int regid;

    @ManyToOne
    private IptMeta metaData;

//    @OneToMany(mappedBy = "inPlantTraining")
//    private List<IptTrainees> iptStudents; // Relationship with IptStudent
}
