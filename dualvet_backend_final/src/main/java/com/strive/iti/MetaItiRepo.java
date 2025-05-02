package com.strive.iti;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface MetaItiRepo extends JpaRepository<UploadMetaDataIti, Integer> {

    // Define a custom query to fetch metadata without eagerly loading associated UploadItiData
   // @Query("SELECT m FROM UploadMetaDataIti m")
  //  List<UploadMetaDataIti> findAllMetadataOnly();
    
	@Query(value = """
		    WITH iti_staging_summary AS (
		        SELECT meta_data_reg_id,
		            COUNT(CASE WHEN status = 'Complete' THEN 1 END) AS complete,
		            COUNT(CASE WHEN status = 'Fail' THEN 1 END) AS fail
		        FROM iti_staging
		        GROUP BY meta_data_reg_id
		    )
		    SELECT meta.reg_id, meta.file_name, meta.created_by, meta.records,
		           meta.status, meta.upload_date, summary.complete, summary.fail
		    FROM iti_staging_summary summary
		    JOIN iti_meta_data meta ON meta.reg_id = summary.meta_data_reg_id
		    """, nativeQuery = true)
		List<Object[]> fetchMetaDataWithSuccessFailCounts();
}