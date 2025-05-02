package com.strive.ipt.master;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IptTraineesRepository extends JpaRepository<IptTrainees, Integer> {
	
	@Query(value = "SELECT count(sno) FROM ipt_trainees ", nativeQuery = true)
    Long findLastSno();
  
}