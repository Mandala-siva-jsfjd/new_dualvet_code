package com.strive.IptReport;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.strive.industry.master.IndustryMasterRepository;
import com.strive.ipt.master.IptInternshipRepository;

@RestController
@RequestMapping("/iptReportGenerateTest")
public class IptReportController {

    @Autowired
    private IndustryMasterRepository industryMasterRepository;
    
    @Autowired
    private IptInternshipRepository iptInternshipRepository;
    
    @GetMapping("/getDistricts")
    public List<String> getDistricts(@RequestParam List<String> states) {
        return industryMasterRepository.findDistrictsByStates(states);
    }
    
    @GetMapping("/getIndustryNames")
    public List<InternshipIndustryNameDTO> getIndustryNames(
            @RequestParam List<String> districts, 
            @RequestParam(required = false) List<Integer> iptYears) {

        // If iptYears is null or empty, default to both years (1 and 2)
        if (iptYears == null || iptYears.isEmpty()) {
            iptYears = Arrays.asList(1, 2);
        }
        
        return iptInternshipRepository.findInternshipDetailsByDistrictsAndYears(districts, iptYears);
    }
}
