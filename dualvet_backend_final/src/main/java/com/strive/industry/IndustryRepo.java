package com.strive.industry;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IndustryRepo extends JpaRepository<Industry, Long> {


	List<Industry> findByRegid(int regid);

	
}
