package com.strive.iti;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.strive.exception.ITIDataAlreadyExistsException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/iti")
@Validated
public class ItiController {

    @Autowired
    private BulkUploadItiService service;

    @PostMapping("/save")
    public ResponseEntity<String> saveBulkData(@Valid @RequestBody UploadMetaDataItiRequest request) {
        try {
            return ResponseEntity.ok(service.saveMDB(request));
        } catch (ITIDataAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }


    @GetMapping("/meta")
    public ResponseEntity<List<UploadMetaDataItiDTO>> getAllMetadata1()
    {
        try {
            List<UploadMetaDataItiDTO> metadataList = service.getAllMetadataOnly1();
            return ResponseEntity.ok(metadataList);
        } catch (Exception e) 
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }
    
    @GetMapping("/metadata/{metaDataRegId}")
    public ResponseEntity<List<UploadItiData>> getItiDataByMetaDataRegId(@PathVariable("metaDataRegId") int metaDataRegId) {
        try {
            List<UploadItiData> itiDataList = service.getItiDataByMetaDataRegId(metaDataRegId);
            if (itiDataList.isEmpty()) 
            {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(itiDataList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    
    @ExceptionHandler(ITIDataAlreadyExistsException.class)
    public ResponseEntity<String> handleITIDataAlreadyExistsException(ITIDataAlreadyExistsException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    
    
    
    @GetMapping("/{metaDataRegId}")
    public ResponseEntity<List<UploadItiDataDto>> getItiDataDtoByMetaDataRegId(@PathVariable("metaDataRegId") int metaDataRegId) {
        try {
 
            List<UploadItiDataDto> itiDataDtoList = service.getItiDataDtoByMetaDataRegId(metaDataRegId);
            if (itiDataDtoList.isEmpty()) 
            {
            	
                return ResponseEntity.noContent().build();
            }
          
            return ResponseEntity.ok(itiDataDtoList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
    
    
    
    
    

