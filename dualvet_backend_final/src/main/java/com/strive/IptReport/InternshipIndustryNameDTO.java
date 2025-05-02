package com.strive.IptReport;

import lombok.Data;

@Data
public class InternshipIndustryNameDTO {
	
	
    private String industryName;

    // Constructor matching the query parameter
    public InternshipIndustryNameDTO(String industryName) {
        this.industryName = industryName;
    }
}
