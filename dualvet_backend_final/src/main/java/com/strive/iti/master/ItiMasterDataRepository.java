package com.strive.iti.master;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.strive.itiReport.ItiDetailsDTO;

@Repository
public interface ItiMasterDataRepository extends JpaRepository<ItiMasterData, Long> {

	
//	@Query("SELECT new com.strive.itiReport.ItiDetailsDTO(" +
//	           "iti.sno, iti.state, iti.district, iti.itiName, iti.itiEmailId, " +
//	           "iti.principalAndVicePrincipal, instr.instructorName, instr.instructorEmailId, " +
//	           "instr.itiTrade, instr.noOfTrainees, instr.noOfSeatsAvailable) " +
//	           "FROM ItiMasterData iti " +
//	           "LEFT JOIN ItiInstructor instr ON instr.itiMasterData.sno = iti.sno " +
//	           "WHERE LOWER(iti.state) IN :states " +
//	           "AND LOWER(iti.district) IN :districts " +
//	           "AND LOWER(iti.itiName) IN :itiNames")
	
	@Query("SELECT new com.strive.itiReport.ItiDetailsDTO(" +
		       "imd.sno, imd.state, imd.district, imd.itiName, imd.itiEmailId, " +
		       "imd.principalAndVicePrincipal, ii.instructorName, ii.instructorEmailId, " +
		       "ii.itiTrade, ii.noOfTrainees, ii.noOfSeatsAvailable) " +
		       "FROM ItiMasterData imd " +
		       "JOIN imd.instructors ii " + // Ensure `instructors` is mapped in ItiMasterData entity
		       "WHERE (:states IS NULL OR imd.state IN :states) " +
		       "AND (:districts IS NULL OR imd.district IN :districts) " +
		       "AND (:itiNames IS NULL OR imd.itiName IN :itiNames)")
		List<ItiDetailsDTO> findItiDetailsByStateDistrictAndName(
		        @Param("states") List<String> states,
		        @Param("districts") List<String> districts,
		        @Param("itiNames") List<String> itiNames);


//    List<ItiMasterData> findItiDetailsByStateDistrictAndName(@Param("states") List<String> states, 
//                                                             @Param("districts") List<String> districts, 
//                                                             @Param("itiNames") List<String> itiNames);
	
	
   

 
}