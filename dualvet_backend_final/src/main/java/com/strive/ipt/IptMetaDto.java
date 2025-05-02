package com.strive.ipt;

import java.time.LocalDateTime;

import com.strive.iti.UploadMetaDataIti;


import lombok.Data;

@Data
public class IptMetaDto 
{
	
		private long regId;
	    private LocalDateTime uploadDate;
	    private int records;
	    private String fileName;
	    private String createdBy;
	    private String updatedBy;
	    private String successRecords;
	    private String failedRecords;	    
	    private IptMeta.UploadStatus status;
	    
	    
	    

}
