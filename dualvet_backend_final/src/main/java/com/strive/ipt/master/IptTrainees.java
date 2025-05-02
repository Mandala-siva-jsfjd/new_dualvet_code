package com.strive.ipt.master;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import com.strive.ipt.InPlantTraining;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "ipt_trainees")
public class IptTrainees {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sno;

    private String traineesName;
    private String emailId;
    private String phoneNumber;
    private String traineesCollegeRegId;
    private String itiName;
    private String traineesItiTrade;
    private String instructorName;
    private String iptDiaryProvided;
    private LocalDateTime createdOn;
    private int createdBy;
    private String updatedBy; 
    private LocalDateTime updatedOn;
    private String remarks;

    @OneToMany(mappedBy = "iptTrainees", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IptInternship> iptInternships;
    
    @Column(name = "meta_reg_id", insertable = false, updatable = false)
    private int regid;

//    @ManyToOne
//    @JoinColumn(name = "inplant_training_id") // Foreign key column in IptStudent table
//    private InPlantTraining inPlantTraining;
}
