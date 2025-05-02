package com.strive.industry.master;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "industry_master_data")
public class IndustryMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sno")
    private Long sno;

//    @Column(name = "id")
//    private Integer id;

    @Column(name = "industry_name")
    private String industryName;

    @Column(name = "contact_name")
    private String contactName;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "email_id")
    private String emailId;

    @Column(name = "nearest_city")
    private String nearestCity;

    @Column(name = "district")
    private String district;

    @Column(name = "state")
    private String state;

    @Column(name = "pincode")
    private Integer pincode;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lan") // This should be "lon"
    private Double lon;

    @Column(name = "designation_of_spoc")
    private String designation_of_spoc;

    @Column(name = "size_of_industry")
    private String sizeOfIndustry;

    @Column(name = "sector")
    private String sector;

//    @Column(name = "domain")
//    private String domain;
    
    @Column(name = "trade")
    private String trade;

    @Column(name = "ojt_support")
    private String ojtSupport;

    @Column(name = "no_of_ojt_trainees")
    private String noOfOjtTrainees;

    @Column(name = "apprenticeship_support")
    private String apprenticeshipSupport;

    @Column(name = "no_of_apprenticeship")
    private String noOfApprenticeship;

    @Column(name = "placements_support")
    private String placementsSupport;

    @Column(name = "no_of_placements")
    private String noOfPlacements;

    @Column(name = "is_stipend_provided")
    private String isStipendProvided;

    @Column(name = "is_transport_provided")
    private String isTransportProvided;

    @Column(name = "is_food_provided")
    private String isFoodProvided;

    @Column(name = "is_accommodation_provided")
    private String isAccommodationProvided;

    @Column(name = "is_mou_sign")
    private String isMouSign;

    @Column(name = "academic_year")
    private String academicYear;
    
    private LocalDateTime createdOn;
    private int createdBy;
    private String updatedBy; 
    private LocalDateTime updatedOn;
    private String remarks;
    
    @Column(name = "meta_reg_id", insertable = false, updatable = false)
    private int regid; // Mapping to the meta_data_reg_id column
}
