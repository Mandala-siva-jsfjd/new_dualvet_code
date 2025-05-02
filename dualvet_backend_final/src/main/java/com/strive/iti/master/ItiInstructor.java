package com.strive.iti.master;

import java.time.LocalDateTime;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "iti_instructors")
public class ItiInstructor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sno;

    @Column(name = "instructor_contact_number")
    private String instructorContactNumber;

    @Column(name = "instructor_email_id", nullable = false)
    private String instructorEmailId;

    @Column(name = "instructor_name")
    private String instructorName;
    
    @Column(name = "course_duration_year")
    private String courseDurationYear;
    
    @Column(name = "iti_trade")
    private String itiTrade;
    
    private String itiName;
    
    @Column(name = "no_of_trainees", nullable = false)
    private int noOfTrainees;
    @Column(name = "no_of_seats_available", nullable = false)
    private int noOfSeatsAvailable;

    // Updated mapping to reference ItiMasterData's sno
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iti_master_data_sno")
    private ItiMasterData itiMasterData;
    @Column(name = "meta_reg_id", insertable = false, updatable = false)
    private int meta_reg_id;
    private LocalDateTime createdOn;
    @Nullable
    @Column(nullable = true) // Allows null values in the database
    private int createdBy;
    private String updatedBy;
    private LocalDateTime updatedOn;
    private String remarks;
    private String district;
    private String state;    
    private String employmentType;
    
}
