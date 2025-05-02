package com.strive.industry.master;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndustryMasterRepository extends JpaRepository<IndustryMaster, Long> {

    // Modified method to find by multiple states and districts
    List<IndustryMaster> findByStateInAndDistrictIn(List<String> state, List<String> district);

    // Method to fetch all distinct states
    @Query("SELECT DISTINCT i.state FROM IndustryMaster i")
    List<String> findAllStates();

    // Method to fetch all distinct districts
    @Query("SELECT DISTINCT i.district FROM IndustryMaster i")
    List<String> findAllDistricts();

    // Method to fetch by multiple states
    List<IndustryMaster> findByStateIn(List<String> states);

    // Method to fetch by multiple districts
    List<IndustryMaster> findByDistrictIn(List<String> districts);

    @Query(value = "SELECT count(sno) FROM industry_master_data ", nativeQuery = true)
    Long findLastSno();
    
    @Query("SELECT DISTINCT i.district FROM IndustryMaster i WHERE i.state IN :states")
    List<String> findDistrictsByStates(@Param("states") List<String> states);

	  


    	
    
}