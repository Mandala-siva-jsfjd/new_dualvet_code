package com.strive.ipt;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.strive.exception.IptAlreadyExistsException;
import com.strive.industry.IndustryDto;
import com.strive.iti.UploadMetaDataItiDTO;


@RestController
@RequestMapping("/ipt")
public class IptController 
{
	@Autowired
	private IptService service;
	
	@PostMapping("/save")
	public ResponseEntity<String> saveBulkData(@RequestBody IptRequest reqest)
	{
		try
		{
			return ResponseEntity.ok(service.saveMDB(reqest));
		}
		catch(IptAlreadyExistsException e)
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		catch(Exception e)
		{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
		}
	}
	
	
	 @GetMapping("/meta")
	    public ResponseEntity<List<IptMetaDto>> getAllMetadata1() {
	        try {
	            List<IptMetaDto> metadataList = service.getMetaData();
	            return ResponseEntity.ok(metadataList);
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
	        }
	    }
	 
	 
	 @GetMapping("/{metaDataRegId}")
	    public ResponseEntity<List<InPlantTrainingDto>> getIndustryData(@PathVariable("metaDataRegId") int metaDataRegId) {
	        try {
	            List<InPlantTrainingDto> ipt = service.getIndustryData(metaDataRegId);
	            if (ipt.isEmpty()) {
	                return ResponseEntity.notFound().build();
	            }
	            return ResponseEntity.ok(ipt);
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	        }
	    }

}
