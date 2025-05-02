 package com.strive.itiReport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.sf.jasperreports.engine.JRException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("itiReport")
public class ReportItiController {

    @Autowired
    private ReportItiService reportItiService;

    @GetMapping("/generateItiReport")
    public ResponseEntity<byte[]> generateItiReport(
            @RequestParam("format") String reportFormat,
            @RequestParam("state") List<String> states,
            @RequestParam("district") List<String> districts,
            @RequestParam("itiName") List<String> itiNames) {

        if (states.isEmpty() || districts.isEmpty() || itiNames.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            // Normalize input parameters to lowercase
            states = states.stream().map(String::toLowerCase).collect(Collectors.toList());
            districts = districts.stream().map(String::toLowerCase).collect(Collectors.toList());
            itiNames = itiNames.stream().map(String::toLowerCase).collect(Collectors.toList());
            
            System.out.println(states);
            System.out.println(districts);
            System.out.println(itiNames);

            byte[] reportData = reportItiService.exportItiReport(reportFormat, states, districts, itiNames);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ItiReport." + reportFormat);
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
