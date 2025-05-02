package com.strive.iti;

import java.time.LocalDateTime;
//import java.util.List;

//import com.strive.iti.master.ItiMasterData;


//import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
//import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
//import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
//import lombok.ToString;

@Data
@Entity
//@ToString(exclude = { "itiMasterData"})
@Table(name = "iti_meta_data")
public class UploadMetaDataIti {

    public enum UploadStatus {
        Pending, Fail, Complete, Loading
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int regId;

    private LocalDateTime uploadDate;
    private int records;
    private String fileName;

    @Enumerated(EnumType.STRING)
    private UploadStatus status;

    private String createdBy;
    private String updatedBy;
//
//    @OneToMany(mappedBy = "uploadMetaData", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<ItiMasterData> itiMasterData;


    // Default constructor
    public UploadMetaDataIti() {
        this.uploadDate = LocalDateTime.now();
        this.records = 0;
        this.status = UploadStatus.Pending;
    }
}
