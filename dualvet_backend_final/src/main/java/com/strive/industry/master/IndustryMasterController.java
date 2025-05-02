package com.strive.industry.master;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class IndustryMasterController {

    private final IndustryMasterRepository industryMasterRepository;

    public IndustryMasterController(IndustryMasterRepository industryMasterRepository) {
        this.industryMasterRepository = industryMasterRepository;
    }

    @GetMapping("/getStateAndDistrict")
    public ResponseEntity<?> get(@RequestParam List<String> state, @RequestParam List<String> district) {
        System.out.println("Fetching industries for states: " + state + " and districts: " + district);
        List<IndustryMaster> industryMasters = industryMasterRepository.findByStateInAndDistrictIn(state, district);

        if (industryMasters.isEmpty()) {
            System.out.println("No industries found for states: " + state + " and districts: " + district);
            return new ResponseEntity<>("not found", HttpStatus.NOT_FOUND);
        }

        System.out.println("Found " + industryMasters.size() + " industries");

        List<IndustryIdAndNameDTO> dtos = industryMasters.stream()
                .map(this::convertToIdAndNameDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/getDistricts")
    public ResponseEntity<?> getDistricts(@RequestParam List<String> state) {
        List<IndustryMaster> districts = industryMasterRepository.findByStateIn(state);

        System.out.println("test");
        if (districts.isEmpty()) {
            return new ResponseEntity<>("not found", HttpStatus.NOT_FOUND);
        }

        List<IndustryStateDto> dtos = districts.stream()
                .map(this::convertToDistrictDTO)
                .distinct()
                .collect(Collectors.toList());

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/getIndustryNameAndId")
    public ResponseEntity<?> getIndustryName(@RequestParam List<String> districts) {
        List<IndustryMaster> obj = industryMasterRepository.findByDistrictIn(districts);

        if (obj.isEmpty()) {
            return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
        }

        List<IndustryDistrictDto> dtos = obj.stream()
                .map(this::convertToIndustryNameAndIdDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }
    
//    @GetMapping("/getAcademicYear")
//    public ResponseEntity<?> getAcademicYear(@RequestParam("industryName") List<String> industryNames) {
//        // Fetch the data based on industry names
//        List<IndustryMaster> industryMasters = industryMasterRepository.findByIndustryNameIn(industryNames);
//
//        if (industryMasters.isEmpty()) {
//            return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
//        }
//
//        // Filter only required fields and return as a list of maps
//        List<Map<String, String>> response = industryMasters.stream()
//                .map(industry -> {
//                    Map<String, String> data = new HashMap<>();
//                    data.put("academicYear", industry.getAcademicYear());
//                    return data;
//                })
//                .collect(Collectors.toList());
//
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }


    

    @GetMapping("/last-sno")
    public Long getLastSno() {
        return industryMasterRepository.findLastSno();
    }

    private IndustryIdAndNameDTO convertToIdAndNameDTO(IndustryMaster industryMaster) {
        IndustryIdAndNameDTO dto = new IndustryIdAndNameDTO();
        dto.setSno(industryMaster.getSno());
        dto.setIndustryName(industryMaster.getIndustryName());
        return dto;
    }

    private IndustryStateDto convertToDistrictDTO(IndustryMaster industryMaster) {
        IndustryStateDto dto = new IndustryStateDto();
        dto.setDistrict(industryMaster.getDistrict());
        return dto;
    }

    private IndustryDistrictDto convertToIndustryNameAndIdDTO(IndustryMaster industryMaster) {
        IndustryDistrictDto dto = new IndustryDistrictDto();
        dto.setSno(industryMaster.getSno());
        dto.setIndustryName(industryMaster.getIndustryName());
        return dto;
    }
    
    
   
    
    
}