package com.strive.ipt.master;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.strive.industry.master.IndustryMaster;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "ipt_internships")
public class IptInternship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sno;

//    @Column(nullable = false, unique = true)
//    private Integer id;

    @Column(nullable = false)
    private Integer iptYear;

    @DateTimeFormat(pattern="dd/MM/yyyy")
    private LocalDate iptStart;
    @DateTimeFormat(pattern="dd/MM/yyyy")
    private LocalDate iptEnd;

    @Column(nullable = false)
    private long stipend;

    private String insuranceStatus;
    private String ppeKitStatus;
    private String academicYear;
//    private String iptAcademicYear;
    private LocalDateTime createdOn;
    private int createdBy;
    private String updatedBy; 
    private LocalDateTime updatedOn;
    private String remarks;

    // Removed the unnecessary industrialId field

    @ManyToOne
    @JoinColumn(name = "industrial_id", referencedColumnName = "sno", nullable = false)
    private IndustryMaster industryMaster;

    @ManyToOne
    @JoinColumn(name = "ipt_trainees_sno", referencedColumnName = "sno", nullable = false)
    private IptTrainees iptTrainees;

    @Column(name = "meta_reg_id", insertable = false, updatable = false)
    private int regid;
    private String employmentType;

    
}
