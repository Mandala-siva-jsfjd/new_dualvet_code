package com.strive.ipt;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Data
//@ToString(exclude = "industry") 
@Table(name = "ipt_meta_data")
public class IptMeta 
{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int  regId;
	private LocalDateTime uploadDate;	
	private int records;	
	private String fileName;
	private String createdBy;
	private String updatedBy;
	@Enumerated(EnumType.STRING)
	private UploadStatus status;
	
	public IptMeta()
	{
		this.uploadDate = LocalDateTime.now();
		this.records = 0;
		this.status = UploadStatus.Pending;
	}
	
	 public enum UploadStatus {
	        Pending, Fail, Complete, Loading
	    }

	
	@OneToMany(mappedBy = "metaData", cascade = CascadeType.ALL)
	private List<InPlantTraining> ipt;

}