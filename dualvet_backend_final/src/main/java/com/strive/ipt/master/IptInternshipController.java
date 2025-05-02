package com.strive.ipt.master;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/iptMaster")
public class IptInternshipController {

	 @Autowired
    private IptTraineesRepository iptTraineesRepository;
    
    
    
    @GetMapping("/iptCount")
    public Long getLastSno()
    {
    	return iptTraineesRepository.findLastSno();
    }
}