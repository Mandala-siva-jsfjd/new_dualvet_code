package com.strive.IptReport;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.sf.jasperreports.engine.JRException;

@RestController
@RequestMapping("iptReportGen")
public class ReportGenerateController {

    @Autowired
    private ReportIptService reportIptService;

    @GetMapping("/generateIptReport")
    public ResponseEntity<byte[]> generateIptReport(
            @RequestParam("format") String reportFormat,
            @RequestParam("industryName") List<String> industryNames) {

        if (industryNames == null || industryNames.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            byte[] reportData = reportIptService.exportIptReport(reportFormat, industryNames);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=industryReport." + reportFormat);
            headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");

            return new ResponseEntity<>(reportData, headers, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Handle invalid format
        } catch (RuntimeException e) {
            // If no data is found or any other runtime issue occurs
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Handle no data found
        } catch (IOException | JRException e) {
            e.printStackTrace(); // Correctly print the stack trace
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Handle other exceptions
        }
    }
}
