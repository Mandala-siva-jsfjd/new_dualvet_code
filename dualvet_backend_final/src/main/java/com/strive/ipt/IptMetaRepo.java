package com.strive.ipt;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IptMetaRepo   extends JpaRepository<IptMeta, Integer>
{

//	@Query("SELECT m FROM IptMeta m")
//	List<IptMeta> findAllMetadataOnly();
	@Query(value = """
			   WITH ipt_staging_summary AS (
    SELECT meta_data_reg_id,
        COUNT(CASE WHEN status = 'Complete' THEN 1 END) AS complete,
        COUNT(CASE WHEN status = 'Fail' THEN 1 END) AS fail
    FROM inplant_training_staging
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
FROM ipt_staging_summary summary
JOIN ipt_meta_data meta
    ON meta.reg_id = summary.meta_data_reg_id;
			    """, nativeQuery = true)
			List<Object[]> fetchMetaDataWithSuccessFailCounts();
	

}
