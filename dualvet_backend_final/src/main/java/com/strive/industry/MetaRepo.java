package com.strive.industry;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
//public interface MetaRepo extends JpaRepository<IndustryMetaData, Integer>
//{
//
//	@Query("SELECT m FROM com.strive.industry.IndustryMetaData m")
//	List<IndustryMetaDataDto> findBymetaDataRegId();
//
//}
public interface MetaRepo extends JpaRepository<IndustryMetaData, Integer> {

//    @Query("SELECT new com.strive.industry.IndustryMetaData(e.regId, e.uploadDate, e.records, e.fileName, e.createdBy, e.status) FROM IndustryMetaData e")
//    List<IndustryMetaDataDto> findBymetaDataRegId();
	@Query(value = """
		   WITH industry_staging_summary AS (
    	        SELECT meta_data_reg_id,
    	            COUNT(CASE WHEN status = 'Complete' THEN 1 END) AS complete,
    	            COUNT(CASE WHEN status = 'Fail' THEN 1 END) AS fail
    	        FROM industry_staging
    	        GROUP BY meta_data_reg_id
    	    )
    	    SELECT 
    	        meta.reg_id AS regId, 
    	        meta.file_name AS fileName, 
    	        meta.created_by AS createdBy, 
    	        meta.records AS records,
    	        meta.status AS status, 
    	        meta.upload_date AS uploadDate, 
    	        summary.complete AS successRecords, 
    	        summary.fail AS failedRecords
    	    FROM industry_staging_summary summary
    	    JOIN industry_meta_data meta 
    	        ON meta.reg_id = summary.meta_data_reg_id
		    """, nativeQuery = true)
		List<Object[]> fetchMetaDataWithSuccessFailCounts();
}


