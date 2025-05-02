package com.strive.iti.master;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;

//import com.strive.iti.UploadMetaDataIti;

@Entity
@Data
@Table(name = "iti_master_data")
public class ItiMasterData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sno;

    private String itiName;
    private String itiEmailId;
    private String district;
    private String state;
    private String address;
    private String principalAndVicePrincipal;
    @Column(name = "principal_contact_number")
    private String principalContactNumber;
//    private String academicYear;
    private LocalDateTime createdOn;
    
    @Nullable
    @Column(nullable = true) // Allows null values in the database
    private Integer createdBy;
    private String updatedBy;
    private LocalDateTime updatedOn;
    private String remarks;
    
    @OneToMany(mappedBy = "itiMasterData", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ItiInstructor> instructors;


//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "meta_data_regid")
//    private UploadMetaDataIti uploadMetaData;

//    @Override
//    public String toString() {
//        return "ItiMasterData [sno=" + sno + ", itiName=" + itiName + ", itiEmailId=" + itiEmailId +
//               ", district=" + district + ", state=" + state + ", address=" + address + 
//               ", principalAndVicePrincipal=" + principalAndVicePrincipal + ",principalContactNumber="+principalContactNumber +
//               ", createdOn=" + createdOn + 
//               ", createdBy=" + createdBy + ", updatedBy=" + updatedBy + 
//               ", updatedOn=" + updatedOn + ", remarks=" + remarks + "]";
//    }
}
