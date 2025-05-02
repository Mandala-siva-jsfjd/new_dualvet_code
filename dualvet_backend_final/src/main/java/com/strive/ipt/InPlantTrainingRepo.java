package com.strive.ipt;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InPlantTrainingRepo  extends JpaRepository<InPlantTraining, Integer>
{

	List<InPlantTraining> findByRegid(int metaDataRegId);

}
