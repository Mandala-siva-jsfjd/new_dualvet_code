package com.strive.industry;

import java.time.LocalDateTime;
import com.strive.industry.IndustryMetaData.UploadStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;


@Data

public class IndustryMetaDataDto 
{
	private int  regId;
	private LocalDateTime uploadDate;	
	private int records;	
	private String fileName;
	private String createdBy;
	private String updatedBy;
	 private String successRecords;
	    private String failedRecords;
	@Enumerated(EnumType.STRING)
	private UploadStatus status;
	   
//	public IndustryMetaDataDto()
//	{
//		this.uploadDate = LocalDateTime.now();
//		this.records = 0;
//		this.status = UploadStatus.Pending;
//	}

}
