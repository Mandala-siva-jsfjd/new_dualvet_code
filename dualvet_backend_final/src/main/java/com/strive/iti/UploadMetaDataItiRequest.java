package com.strive.iti;

import java.util.List;

import lombok.Data;

@Data
public class UploadMetaDataItiRequest 
{
	 private String fileName;

	 private List<UploadItiData> itiList;

}
