package com.strive.itiReport;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.strive.iti.master.ItiMasterDataRepository;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ReportItiService {

    private static final Logger logger = LoggerFactory.getLogger(ReportItiService.class);

    @Autowired
    private ItiMasterDataRepository repository;

    @Autowired
    private ResourceLoader resourceLoader;

    public byte[] exportItiReport(String reportFormat, List<String> states, List<String> districts, List<String> itiNames) throws JRException, IOException {
        logger.info("Starting exportItiReport with parameters: format={}, states={}, districts={}, itiNames={}",
                    reportFormat, states, districts, itiNames);

        if (states.isEmpty() || districts.isEmpty() || itiNames.isEmpty()) {
            logger.error("One of the required parameters is empty: states={}, districts={}, itiNames={}", states, districts, itiNames);
            throw new IllegalArgumentException("States, districts, and ITI names cannot be empty");
        }

        List<ItiDetailsDTO> itiDetails = repository.findItiDetailsByStateDistrictAndName(states, districts, itiNames);
        itiDetails.forEach(System.out::println);


        if (itiDetails.isEmpty()) {
            logger.error("No data found for the given states, districts, and ITI names.");
            throw new RuntimeException("No data found for the given states, districts, and ITI names.");
        }
        
        // Generate report
        Resource resource = resourceLoader.getResource("classpath:ItiReport.jrxml");
        File file = resource.getFile();

        if (file == null || !file.exists()) {
            logger.error("Jasper report file not found: {}", resource.getFilename());
            throw new IOException("Jasper report file not found: " + resource.getFilename());
        }

        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(itiDetails);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Jogendra");

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        switch (reportFormat.toLowerCase()) {
            case "pdf":
                JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
                break;
            case "xml":
                JasperExportManager.exportReportToXmlStream(jasperPrint, outputStream);
                break;
            case "xlsx":
                JRXlsxExporter exporter = new JRXlsxExporter();
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
                SimpleXlsxReportConfiguration config = new SimpleXlsxReportConfiguration();
                config.setOnePagePerSheet(false);
                config.setRemoveEmptySpaceBetweenRows(true);
                config.setWhitePageBackground(false);
                exporter.setConfiguration(config);
                exporter.exportReport();
                break;
            default:
                logger.error("Invalid format provided: {}", reportFormat);
                throw new IllegalArgumentException("Invalid format: " + reportFormat);
        }

        logger.info("Report generated successfully in format: {}", reportFormat);
        return outputStream.toByteArray();
    }

}
