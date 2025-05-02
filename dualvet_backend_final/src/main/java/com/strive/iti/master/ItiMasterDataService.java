package com.strive.iti.master;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItiMasterDataService {

    @Autowired
    private ItiMasterDataRepository itiMasterDataRepository;
    
    @Autowired
    private ItiInstructorRepository instructorRepo;
    
    

    @Transactional(readOnly = true)
    public List<String> getDistrictsByStates(List<String> states) {
        // Query the repository for ITI records matching the given states
        List<ItiInstructor> itiList = instructorRepo.findAllByStateIn(states);
        
        // Extract distinct district names from the result
        return itiList.stream()
                .map(ItiInstructor::getDistrict)
                .distinct()
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<String> getItiNamesByStatesAndDistricts(List<String> states, List<String> districts) {
        List<ItiInstructor> itiList = instructorRepo.findAllByStateIn(states);

        // Filter districts that match the provided states
        List<String> validDistricts = itiList.stream()
                .map(ItiInstructor::getDistrict)
                .filter(districts::contains)
                .distinct()
                .collect(Collectors.toList());

        // If no valid districts are found, return an empty list
        if (validDistricts.isEmpty()) {
            return Collections.emptyList();
        }

        // Fetch ITI names for valid state-district combinations
        List<ItiInstructor> filteredItiList = instructorRepo.findAllByStateInAndDistrictIn(states, validDistricts);
        
        return filteredItiList.stream()
                .map(ItiInstructor::getItiName)
                .distinct()
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Long getDistinctItiNameCount() {
        return instructorRepo.findDistinctItiNameCount();
    }

}