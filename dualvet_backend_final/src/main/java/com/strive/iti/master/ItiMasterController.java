package com.strive.iti.master;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/itiMaster")
public class ItiMasterController {

    @Autowired
    private ItiMasterDataService itiMasterDataService;
    
    @Autowired
    private ItiInstructorRepository itiInstructorRepository;

    // Endpoint to get districts by state
    @GetMapping("/districts")
    public ResponseEntity<?> getDistrictsByStates(@RequestParam List<String> state) {
        if (state == null || state.isEmpty()) {
            return ResponseEntity.badRequest().body("States parameter is required.");
        }

        List<String> districts = itiMasterDataService.getDistrictsByStates(state);

        if (districts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No districts found for the given states.");
        }

        return ResponseEntity.ok(districts);
    }

    @GetMapping("/itiNames")
    public ResponseEntity<?> getItiNamesByStatesAndDistricts(
            @RequestParam List<String> state,
            @RequestParam List<String> district) {
        if (state == null || state.isEmpty() || district == null || district.isEmpty()) {
            return ResponseEntity.badRequest().body("States and districts parameters are required.");
        }

        List<String> itiNames = itiMasterDataService.getItiNamesByStatesAndDistricts(state, district);

        if (itiNames.isEmpty()) {
            // Return an empty list instead of a 404 error
            return ResponseEntity.ok(Collections.emptyList());
        }

        return ResponseEntity.ok(itiNames);
    }


    // Endpoint to get the last SNO
    @GetMapping("/Iticount")
    public ResponseEntity<Long> getLastSno() {
        Long lastSno = itiMasterDataService.getDistinctItiNameCount();
        if (lastSno == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(lastSno);
    }
    
    @GetMapping("/allStates")
    public ResponseEntity<List<String>> dynamicState() {
        try {
            List<String> stateList = itiInstructorRepository.findDynamicStates();
            return ResponseEntity.ok(stateList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}