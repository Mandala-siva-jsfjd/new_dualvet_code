package com.strive.industry;

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

import com.strive.exception.IndustryAlreadyExistsException;

@RestController
@RequestMapping("/industry")
public class IndustryController 
{
	@Autowired
	IndustryBulkService service;
	
	@PostMapping("/save")
	public ResponseEntity<String> saveBulkData(@RequestBody IndustryRequest reqest)
	{
		try
		{
			return ResponseEntity.ok(service.saveMDB(reqest));
		}
		catch(IndustryAlreadyExistsException e)
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		catch(Exception e)
		{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
		}
	}

	@GetMapping("/meta")
	public ResponseEntity<List<IndustryMetaDataDto>> getAllMetaData() {
	    try {
	        List<IndustryMetaDataDto> metaDataList = service.getMetaData();
	        
	        return ResponseEntity.ok(metaDataList);
	    } catch (Exception e) {
	        // Log the exception details
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
	}

	
	@GetMapping("/{metaDataRegId}")
	public ResponseEntity<List<IndustryDto>> getIndustryData(@PathVariable("metaDataRegId") int metaDataRegId)
	{
		try
		{
			List<IndustryDto> industryList = service.getIndustryData(metaDataRegId);
			if(industryList.isEmpty())
			{
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(industryList);
		}
		catch (Exception e) 
		{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	
}
