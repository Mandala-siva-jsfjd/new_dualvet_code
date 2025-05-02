package com.strive.iti.master;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ItiInstructorRepository extends JpaRepository<ItiInstructor, Long> {

	   @Query("SELECT DISTINCT i.state FROM ItiInstructor i")
	    List<String> findDynamicStates();
	   
	   List<ItiInstructor> findAllByStateIn(List<String> states);

	    List<ItiInstructor> findAllByStateInAndDistrictIn(List<String> states, List<String> districts);
	    
	    @Query(value = "SELECT COUNT(iti_name) FROM iti_master_data", nativeQuery = true)
	    Long findDistinctItiNameCount();

}
