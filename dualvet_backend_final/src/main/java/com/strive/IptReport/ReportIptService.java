package com.strive.IptReport;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.strive.ipt.master.IptInternshipRepository;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ReportIptService {

    private static final Logger logger = LoggerFactory.getLogger(ReportIptService.class);

    @Autowired
    private IptInternshipRepository repository;

    @Autowired
    private ResourceLoader resourceLoader;

    public byte[] exportIptReport(String reportFormat, List<String> industryNames) throws JRException, IOException {
        logger.info("Starting exportIptReport with parameters: format={}, industryNames={}", reportFormat, industryNames);

        System.out.println("test");
        if (industryNames == null || industryNames.isEmpty()) {
            logger.error("Industry names list is empty.");
            throw new IllegalArgumentException("Industry names cannot be empty");
        }

        List<IptReportDto> reportData = repository.findReportByIndustryNames(industryNames);

        System.out.println(reportData);
        if (reportData.isEmpty()) {
            logger.error("No data found for the given industry names: {}", industryNames);
            throw new RuntimeException("No data found for the given industry names.");
        }

        // Generate report
        Resource resource = resourceLoader.getResource("classpath:IptReport.jrxml");
        File file = resource.getFile();

        if (file == null || !file.exists()) {
            logger.error("Jasper report file not found: {}", resource.getFilename());
            throw new IOException("Jasper report file not found: " + resource.getFilename());
        }

        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportData);
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

        System.out.println("testing the report");
        logger.info("Report generated successfully in format: {}", reportFormat);
        return outputStream.toByteArray();
    }
}