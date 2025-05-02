package com.strive.ipt;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.strive.industry.IndustryMetaData;
import com.strive.industry.IndustryMetaDataDto;
import com.strive.industry.master.IndustryMaster;
import com.strive.industry.master.IndustryMasterRepository;
import com.strive.model.User;
import com.strive.model.UserRepository;
import jakarta.transaction.Transactional;


@Service
public class IptService
{
	@Autowired
	private IptMetaRepo metaRepo;
	@Autowired
	private InPlantTrainingRepo repo;
	@Autowired
	UserRepository userRepository;
	@Autowired
	IndustryMasterRepository industryRepo;
	
	@Transactional
	public String saveMDB(@RequestBody IptRequest request)
	{
		try {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found for email: " + currentUserEmail));

        IptMeta metaData = new IptMeta ();
        metaData.setFileName(request.getFileName());
        metaData.setCreatedBy(user.getFirstname());
        metaData.setUploadDate(LocalDateTime.now());
        
        List<InPlantTraining> ipt = request.getIpt();
        
        if(ipt != null)
        {
        	metaData.setRecords(ipt.size());
        }
        else
        {
        	metaData.setRecords(0);
        }
		metaRepo.save(metaData);
		if(request.getIpt() != null && !request.getIpt().isEmpty())
		{
			saveAssociatedData(metaData, request.getIpt());
		}
		return "META AND INDUSTRY DATA IS SAVED SUCCESSFULLY";
		}
	catch(Exception e)
	{
		return "Error occurred while saving metadata:"+ e.getMessage();
	}
}
		
		
		
		private void saveAssociatedData(IptMeta metaData, List<InPlantTraining> ipt)
		{
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        String currentUserEmail = authentication.getName();

	        User user = userRepository.findByEmail(currentUserEmail)
	                .orElseThrow(() -> new RuntimeException("User not found for email: " + currentUserEmail));
			for(InPlantTraining data : ipt)
			{
				data.setMetaData(metaData);
				data.setCreatedBy(user.getId());
				
			
				repo.save(data);
			}
		
	}
		
//	
//		@Transactional
//		public List<IptMetaDto> getMetaData() {
//		
//		    List<IptMeta> metaDataEntities = metaRepo.findAll();
//		    List<IptMetaDto> metaDataList = new ArrayList<>();
//		    for (IptMeta entity : metaDataEntities) {
//		        IptMetaDto dto = new IptMetaDto();
//		        dto.setRegId(entity.getRegId());
//		        dto.setUploadDate(entity.getUploadDate());
//		        dto.setRecords(entity.getRecords());
//		        dto.setFileName(entity.getFileName());
//		        dto.setCreatedBy(entity.getCreatedBy());
//		        dto.setUpdatedBy(entity.getUpdatedBy());
//		        dto.setStatus(entity.getStatus());
//		        metaDataList.add(dto);
//		    }
//		    return metaDataList;
//		}
		
		 @Transactional
		 public List<IptMetaDto> getMetaData() {
		        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		        String currentUserEmail = authentication.getName();
	
	        userRepository.findByEmail(currentUserEmail)
		                .orElseThrow(() -> new RuntimeException("User not found for email: " + currentUserEmail));

		        List<Object[]> result = metaRepo.fetchMetaDataWithSuccessFailCounts();
		        List<IptMetaDto> dtoList = new ArrayList<>();

		        for (Object[] row : result) {
		        	IptMetaDto dto = new IptMetaDto();

		            dto.setRegId(((Number) row[0]).longValue()); // meta.reg_id
		            dto.setFileName((String) row[1]);             // file_name
		            dto.setCreatedBy((String) row[2]);            // created_by
		            dto.setRecords(((Number) row[3]).intValue()); // records
		            dto.setStatus(IptMeta.UploadStatus.valueOf((String) row[4]));
		            dto.setUploadDate(((Timestamp) row[5]).toLocalDateTime());             // upload_date
		            dto.setSuccessRecords(String.valueOf(((Number) row[6]).intValue()));   // complete
		            dto.setFailedRecords(String.valueOf(((Number) row[7]).intValue()));    // fail

		            dtoList.add(dto);
		        }

		        return dtoList;
		    }
		
		
		 @Transactional
		    public List<InPlantTrainingDto> getIndustryData(int metaDataRegId) {
		        List<InPlantTraining> ipt = repo.findByRegid(metaDataRegId);
		        return ipt.stream().map(this::convertToDto).collect(Collectors.toList());
		    }

		    private InPlantTrainingDto convertToDto(InPlantTraining data) 
		    {
		        InPlantTrainingDto dto = new InPlantTrainingDto();
		        dto.setId(data.getId());
		        dto.setTraineesName(data.getTraineesName());
		        dto.setTraineesCollegeRegId(data.getTraineesCollegeRegId());
		        dto.setTraineesItiTrade(data.getTraineesItiTrade());
		        dto.setPhoneNumber(data.getPhoneNumber());
		        dto.setEmailId(data.getEmailId());
		        dto.setStipend(data.getStipend());
		        dto.setItiName(data.getItiName());
		        dto.setIndustryName(data.getIndustryName());
		        dto.setInstructorName(data.getInstructorName());
		        dto.setIptYear(data.getIptYear());
		        dto.setIptStart(data.getIptStart());
		        dto.setIptEnd(data.getIptEnd());
		        dto.setInsuranceStatus(data.getInsuranceStatus());
		        dto.setPpeKitStatus(data.getPpeKitStatus());
		        dto.setIptDiaryProvided(data.getIptDiaryProvided());
		        dto.setCreatedBy(data.getCreatedBy());
		        dto.setCreatedOn(data.getCreatedOn());
		        dto.setUpdatedBy(data.getUpdatedBy());
		        dto.setUpdatedOn(data.getUpdatedOn());
		        dto.setRemarks(data.getRemarks());
		        dto.setStatus(data.getStatus());
		        dto.setAcademicYear(data.getAcademicYear());
		        dto.setEmploymentType(data.getEmploymentType());
		        return dto;
		    }
}








