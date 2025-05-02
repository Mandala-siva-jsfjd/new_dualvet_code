package com.strive.iti;

import java.time.LocalDateTime;

import lombok.Data;

@Data

public class UploadMetaDataItiDTO {
    private long regId;
    private LocalDateTime uploadDate;
    private int records;
    private String fileName;
    private UploadMetaDataIti.UploadStatus status;
    private String createdBy;
    private String updatedBy;
    private String successRecords;
    private String failedRecords;
	

}