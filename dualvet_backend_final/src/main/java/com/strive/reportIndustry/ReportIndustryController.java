package com.strive.reportIndustry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReportIndustryController {

    @Autowired
    private ReportIndustryService reportIndustryService;

    @GetMapping("/generateReport")
    public ResponseEntity<byte[]> generateReport(
            @RequestParam("format") String reportFormat,
            @RequestParam("states") List<String> states,
            @RequestParam("districts") List<String> districts) {

        if (states == null || states.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (districts == null || districts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            byte[] reportData = reportIndustryService.exportReport(reportFormat, states, districts);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=IndustryReport." + reportFormat);
            headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");

            return new ResponseEntity<>(reportData, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace(); // Print the stack trace for debugging
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}