package com.strive.ipt.master;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.strive.IptReport.InternshipIndustryNameDTO;
import com.strive.IptReport.IptReportDto;

@Repository
public interface IptInternshipRepository extends JpaRepository<IptInternship, Integer> 
{
    
    @Query("SELECT new com.strive.IptReport.InternshipIndustryNameDTO(im.industryName) " +
           "FROM IptInternship ii " +
           "JOIN ii.industryMaster im " +
           "WHERE im.district IN :districts " +
           "AND ii.iptYear IN :iptYears")
    List<InternshipIndustryNameDTO> findInternshipDetailsByDistrictsAndYears(
            @Param("districts") List<String> districts, 
            @Param("iptYears") List<Integer> iptYears);
    
//    @Query("SELECT DISTINCT new com.strive.IptReport.IptReportDto(" +
//            "i.sno, " +
//            "i.industryMaster.sno, " +
//            "i.iptYear, " +
//            "s.itiName, " +
//            "s.instructorName, " +
//            "s.traineesName, " +
//            "s.traineesCollegeRegId, " +
//            "s.phoneNumber, " +
//            "s.emailId, " +
//            "s.traineesItiTrade, " +
//            "i.stipend, " +
//            "i.iptStart, " +
//            "i.iptEnd) " +
//            "FROM IptInternship i " +
//            "JOIN i.iptTrainees s " +
//            "JOIN i.industryMaster imd " +
//            "WHERE imd.industryName IN :industryNames")
//    List<IptReportDto> findReportByIndustryNames(@Param("industryNames") List<String> industryNames);
//    
    @Query("SELECT DISTINCT new com.strive.IptReport.IptReportDto(" +
            "i.sno, " +
            "imd.sno, " +  // Ensure the ID field in IndustryMaster is correct
            "i.iptYear, " +
            "s.itiName, " +
            "s.instructorName, " +
            "s.traineesName, " +
            "s.traineesCollegeRegId, " +
            "s.phoneNumber, " +
            "s.emailId, " +
            "s.traineesItiTrade, " +
            "i.stipend, " +
            "i.iptStart, " +
            "i.iptEnd) " +
            "FROM IptInternship i " +
            "JOIN i.iptTrainees s " +
            "JOIN i.industryMaster imd " +
            "WHERE imd.industryName IN :industryNames")
    List<IptReportDto> findReportByIndustryNames(@Param("industryNames") List<String> industryNames);

    
   


}