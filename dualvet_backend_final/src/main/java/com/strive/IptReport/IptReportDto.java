package com.strive.IptReport;


import java.time.LocalDate;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IptReportDto {
    private Integer sno;
    private Long industrialId;
    private Integer iptYear;
    private String itiName;
    private String instructorName;
    private String traineesName;
    private String traineesCollegeRegId;
    private String phoneNumber;
    private String emailId;
    private String traineesItiTrade;
    private Long stipend;
    private LocalDate iptStart;
    private LocalDate iptEnd;  
    
    public IptReportDto(Integer sno, Long industrialId, Integer iptYear, String itiName, String instructorName, 
            String traineesName, String traineesCollegeRegId, String phoneNumber, String emailId, 
            String traineesItiTrade, Long stipend, LocalDate iptStart, LocalDate iptEnd) {
this.sno = sno;
this.industrialId = industrialId;
this.iptYear = iptYear;
this.itiName = itiName;
this.instructorName = instructorName;
this.traineesName = traineesName;
this.traineesCollegeRegId = traineesCollegeRegId;
this.phoneNumber = phoneNumber;
this.emailId = emailId;
this.traineesItiTrade = traineesItiTrade;
this.stipend = stipend;
this.iptStart = iptStart;
this.iptEnd = iptEnd;
}    
}
