package com.strive.industry;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.strive.iti.UploadMetaDataIti;
import com.strive.iti.UploadMetaDataItiDTO;
import com.strive.model.User;
import com.strive.model.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class IndustryBulkService 
{
	@Autowired
	IndustryRepo repo;
	
	@Autowired
	MetaRepo metaRepo;
	
	@Autowired
	UserRepository userRepository;
	
	
	@Transactional
	public String saveMDB(@RequestBody IndustryRequest request)
	{
		try {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found for email: " + currentUserEmail));

        IndustryMetaData metaData = new IndustryMetaData();
        metaData.setFileName(request.getFileName());
        metaData.setCreatedBy(user.getFirstname());
        metaData.setUploadDate(LocalDateTime.now());
        
        List<Industry> ind = request.getInd();
        
        if(ind != null)
        {
        	metaData.setRecords(ind.size());
        }
        else
        {
        	metaData.setRecords(0);
        }
		metaRepo.save(metaData);
		
		if(request.getInd() != null && !request.getInd().isEmpty())
		{
			saveAssociatedData(metaData, request.getInd());
		}
		return "META AND INDUSTRY DATA IS SAVED SUCCESSFULLY";
		}
		catch(Exception e)
		{
			return "Error occurred while saving metadata:"+ e.getMessage();
		}
	}


	private void saveAssociatedData(IndustryMetaData metaData, List<Industry> ind)
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentUserEmail = authentication.getName();

    User user = userRepository.findByEmail(currentUserEmail)
            .orElseThrow(() -> new RuntimeException("User not found for email: " + currentUserEmail));

		for(Industry data : ind)
		{
			data.setMetaData(metaData);
			data.setCreatedBy(user.getId());
			repo.save(data);
		}
		
	}
	
//	@Transactional
//	public List<IndustryMetaDataDto> getMetaData() {
//	   
//	    List<IndustryMetaData> metaDataEntities = metaRepo.findAll();
//	    List<IndustryMetaDataDto> metaDataList = new ArrayList<>();
//	    for (IndustryMetaData entity : metaDataEntities) {
//	        IndustryMetaDataDto dto = new IndustryMetaDataDto();
//	        dto.setRegId(entity.getRegId());
//	        dto.setUploadDate(entity.getUploadDate());
//	        dto.setRecords(entity.getRecords());
//	        dto.setFileName(entity.getFileName());
//	        dto.setCreatedBy(entity.getCreatedBy());
//	        dto.setUpdatedBy(entity.getUpdatedBy());
//	        dto.setStatus(entity.getStatus());
//	        metaDataList.add(dto);
//	    }
//	    return metaDataList;
//	}
	 @Transactional
	 public List<IndustryMetaDataDto> getMetaData() {
//	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//	        String currentUserEmail = authentication.getName();
//
//	        userRepository.findByEmail(currentUserEmail)
//	                .orElseThrow(() -> new RuntimeException("User not found for email: " + currentUserEmail));

	        List<Object[]> result = metaRepo.fetchMetaDataWithSuccessFailCounts();
	        List<IndustryMetaDataDto> dtoList = new ArrayList<>();

	        for (Object[] row : result) {
	        	IndustryMetaDataDto dto = new IndustryMetaDataDto();

	            dto.setRegId(((Number) row[0]).intValue()); // meta.reg_id
	            dto.setFileName((String) row[1]);             // file_name
	            dto.setCreatedBy((String) row[2]);            // created_by
	            dto.setRecords(((Number) row[3]).intValue()); // records
	            dto.setStatus(IndustryMetaData.UploadStatus.valueOf((String) row[4])); // status
	            dto.setUploadDate(((Timestamp) row[5]).toLocalDateTime());             // upload_date
	            dto.setSuccessRecords(String.valueOf(((Number) row[6]).intValue()));   // complete
	            dto.setFailedRecords(String.valueOf(((Number) row[7]).intValue()));    // fail

	            dtoList.add(dto);
	        }

	        return dtoList;
	    }

	
	 @Transactional
	    public List<IndustryDto> getIndustryData(int metaDataRegId) {
	        List<Industry> industryList = repo.findByRegid(metaDataRegId);
	        return industryList.stream().map(this::convertToDto).collect(Collectors.toList());
	    }

	    private IndustryDto convertToDto(Industry data) 
	    {
	    		
	        IndustryDto dto = new IndustryDto();
	        dto.setId(data.getId());
	        dto.setIndustryName(data.getIndustryName());
	        dto.setContactName(data.getContactName());
	        dto.setContactNumber(data.getContactNumber());
	        dto.setEmailId(data.getEmailId());
	        dto.setNearestCity(data.getNearestCity());
	        dto.setDistrict(data.getDistrict());
	        dto.setState(data.getState());
	        dto.setPincode(data.getPincode());
	        dto.setLat(data.getLat());
	        dto.setLan(data.getLan());
	        dto.setDesignationOfSpoc(data.getDesignationOfSpoc());
	        dto.setSizeOfIndustry(data.getSizeOfIndustry());
	        dto.setSector(data.getSector());
//	        dto.setDomain(data.getDomain());
	        dto.setTrade(data.getTrade());
	        dto.setOjtSupport(data.getOjtSupport());
	        dto.setNoOfOjtTrainees(data.getNoOfOjtTrainees());
	        dto.setPlacementsSupport(data.getPlacementsSupport());
	        dto.setNoOfPlacements(data.getNoOfPlacements());
	        dto.setApprenticeshipSupport(data.getApprenticeshipSupport());
	        dto.setNoOfApprenticeship(data.getNoOfApprenticeship());
	        dto.setIsStipendProvided(data.getIsStipendProvided());
	        dto.setIsTransportProvided(data.getIsTransportProvided());
	        dto.setIsFoodProvided(data.getIsFoodProvided());
	        dto.setIsAccommodationProvided(data.getIsAccommodationProvided());
	        dto.setIsMouSign(data.getIsMouSign());
	        dto.setAcademicYear(data.getAcademicYear());
	        dto.setCreatedBy(data.getCreatedBy());
	        dto.setCreatedOn(data.getCreatedOn());
	        dto.setUpdatedBy(data.getUpdatedBy());
	        dto.setUpdatedOn(data.getUpdatedOn());
	        dto.setRemarks(data.getRemarks());
	        dto.setStatus(data.getStatus());
   
	        return dto;   
	    }   
	    }