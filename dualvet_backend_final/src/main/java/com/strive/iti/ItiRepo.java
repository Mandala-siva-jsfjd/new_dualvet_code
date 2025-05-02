package com.strive.iti;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ItiRepo extends JpaRepository<UploadItiData, Integer>{


	List<UploadItiData> findBymetaDataRegId(int metaDataRegId);


}
