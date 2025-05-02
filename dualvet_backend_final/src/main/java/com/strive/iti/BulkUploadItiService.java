package com.strive.iti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.strive.model.User;
import com.strive.model.UserRepository;
import jakarta.transaction.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BulkUploadItiService
{

    @Autowired
    private MetaItiRepo metaItiRepo;

    @Autowired
    private ItiRepo itiRepo;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public String saveMDB(UploadMetaDataItiRequest request) 
    {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUserEmail = authentication.getName();

            User user = userRepository.findByEmail(currentUserEmail)
                    .orElseThrow(() -> new RuntimeException("User not found for email: " + currentUserEmail));

            UploadMetaDataIti metaData = new UploadMetaDataIti();
            metaData.setFileName(request.getFileName());
            metaData.setCreatedBy(user.getFirstname());
            metaData.setUploadDate(LocalDateTime.now());
            
            List<UploadItiData> itiList = request.getItiList();
            if (itiList != null) {
                metaData.setRecords(itiList.size());
            } else {
                metaData.setRecords(0);
            }

            metaItiRepo.save(metaData);

            if (request.getItiList() != null && !request.getItiList().isEmpty()) //condition for iti master data is not null
            {
               
                saveAssociatedItiData(metaData, request.getItiList());
            }

            return "Metadata and ITI data saved successfully";
        } catch (Exception e) {
            return "Error occurred while saving metadata: " + e.getMessage();
        }
    }

   

    private void saveAssociatedItiData(UploadMetaDataIti metaData, List<UploadItiData> itiList) 
    {
    	 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         String currentUserEmail = authentication.getName();

         User user = userRepository.findByEmail(currentUserEmail)
                 .orElseThrow(() -> new RuntimeException("User not found for email: " + currentUserEmail));

    	
        for (UploadItiData data : itiList) {
            data.setMetaData(metaData);
            data.setCreatedBy(user.getId());
            itiRepo.save(data);
        }
    }

    
    @Transactional
    public List<UploadMetaDataItiDTO> getAllMetadataOnly1() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found for email: " + currentUserEmail));

        List<Object[]> result = metaItiRepo.fetchMetaDataWithSuccessFailCounts();
        List<UploadMetaDataItiDTO> dtoList = new ArrayList<>();

        for (Object[] row : result) {
            UploadMetaDataItiDTO dto = new UploadMetaDataItiDTO();

            dto.setRegId(((Number) row[0]).longValue()); // meta.reg_id
            dto.setFileName((String) row[1]);             // file_name
            dto.setCreatedBy((String) row[2]);            // created_by
            dto.setRecords(((Number) row[3]).intValue()); // records
            dto.setStatus(UploadMetaDataIti.UploadStatus.valueOf((String) row[4])); // status
            dto.setUploadDate(((Timestamp) row[5]).toLocalDateTime());             // upload_date
            dto.setSuccessRecords(String.valueOf(((Number) row[6]).intValue()));   // complete
            dto.setFailedRecords(String.valueOf(((Number) row[7]).intValue()));    // fail

            dtoList.add(dto);
        }

        return dtoList;
    }



//    @Transactional
//    public List<UploadMetaDataItiDTO> getAllMetadataOnly1() {
//    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String currentUserEmail = authentication.getName();
//
//        User user = userRepository.findByEmail(currentUserEmail)
//                .orElseThrow(() -> new RuntimeException("User not found for email: " + currentUserEmail));
//
//    	
//        List<UploadMetaDataIti> metadataList = metaItiRepo.findAllMetadataOnly();
//        return metadataList.stream().map(metaData -> {
//            UploadMetaDataItiDTO dto = new UploadMetaDataItiDTO();
//            dto.setRegId(metaData.getRegId());
//            dto.setUploadDate(metaData.getUploadDate());
//            dto.setRecords(metaData.getRecords());
//            dto.setFileName(metaData.getFileName());
//            dto.setStatus(metaData.getStatus());
//            dto.setCreatedBy(metaData.getCreatedBy());
//            dto.setUpdatedBy(metaData.getUpdatedBy());
//            return dto;
//        }).collect(Collectors.toList());
//    }
    
    
    @Transactional
    public List<UploadItiData> getItiDataByMetaDataRegId(int metaDataRegId) {
        return itiRepo.findBymetaDataRegId(metaDataRegId);
    }

	
    @Transactional
    public List<UploadItiDataDto> getItiDataDtoByMetaDataRegId(int metaDataRegId) {
    	System.out.println("3");
        List<UploadItiData> itiDataList = itiRepo.findBymetaDataRegId(metaDataRegId);
        System.out.println("4");
        return itiDataList.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private UploadItiDataDto convertToDto(UploadItiData data) {
    	
    	 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         String currentUserEmail = authentication.getName();

         User user = userRepository.findByEmail(currentUserEmail)
                 .orElseThrow(() -> new RuntimeException("User not found for email: " + currentUserEmail));

    	
        UploadItiDataDto dto = new UploadItiDataDto();
        dto.setId(data.getId());
        dto.setItiName(data.getItiName());
        dto.setState(data.getState());
        dto.setDistrict(data.getDistrict());
//        dto.setAddress(data.getAddress());
        dto.setInstructorEmailId(data.getInstructorEmailId());
//        dto.setItiEmailId(data.getItiEmailId());
        dto.setInstructorName(data.getInstructorName());
//        dto.setPrincipalContactNumber(data.getPrincipalContactNumber());
        dto.setItiTrade(data.getItiTrade());
        dto.setCreatedBy(data.getCreatedBy());
        dto.setCreatedOn(data.getCreatedOn());
        dto.setUpdatedBy(data.getUpdatedBy());
        dto.setUpdatedOn(data.getUpdatedOn());
        dto.setRemarks(data.getRemarks());
//        dto.setPrincipalAndVicePrincipal(data.getPrincipalAndVicePrincipal());
        dto.setNoOfTrainees(data.getNoOfTrainees());
        dto.setNoOfSeatsAvailable(data.getNoOfSeatsAvailable());
        dto.setCourseDurationYear(data.getCourseDurationYear());
        dto.setInstructorContactNumber(data.getInstructorContactNumber());
        dto.setStatus(data.getStatus());
        dto.setEmploymentType(data.getEmploymentType());

        return dto;
    }
    
}
