package com.strive.reportIndustry;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.strive.industry.master.IndustryMaster;
import com.strive.industry.master.IndustryMasterRepository;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@Service
public class ReportIndustryService {

    @Autowired
    private IndustryMasterRepository repository;
    @Autowired
    private ResourceLoader resourceLoader;

    public byte[] exportReport(String reportFormat, List<String> states, List<String> districts) throws JRException, IOException {
        List<IndustryReportDto> allIndustryReportDtos = new ArrayList<>();

        // Handle "allStates" and "allDistricts" cases
        if (states.contains("allStates")) {
            states = repository.findAllStates();
        }
        if (districts.contains("allDistricts")) {
            districts = repository.findAllDistricts();
        }

        List<IndustryMaster> industryDetails = repository.findByStateInAndDistrictIn(states, districts);

        if (industryDetails.isEmpty()) {
            throw new RuntimeException("No data found for the given states and districts.");
        }

     // Mapping industry details to DTO
        allIndustryReportDtos = industryDetails.stream()
            .map(industry -> new IndustryReportDto(
                industry.getSno(),
                industry.getIndustryName(),
                industry.getContactName(),
                industry.getContactNumber(),
                industry.getEmailId(),
                industry.getNearestCity(),
                industry.getDistrict(),
                industry.getState(),
                industry.getPincode(),
                industry.getLat(),
                industry.getLon(),
                industry.getDesignation_of_spoc(),  // Fixed field name
                industry.getSizeOfIndustry(),
                industry.getSector(),
//                industry.getDomain(),
                industry.getOjtSupport(),
                industry.getNoOfOjtTrainees(),
                industry.getApprenticeshipSupport(),
                industry.getNoOfApprenticeship(),
                industry.getPlacementsSupport(),
                industry.getNoOfPlacements(),
                industry.getIsStipendProvided(),  // Fixed field name
                industry.getIsTransportProvided(),
                industry.getIsFoodProvided(),
                industry.getIsAccommodationProvided(),
                industry.getIsMouSign(),
                null // Adding academicYear, as it's not retrieved from the current industry object
            ))
            .collect(Collectors.toList());


        // Compile and fill the report
        Resource resource = resourceLoader.getResource("classpath:IndustryReport.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(resource.getInputStream());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(allIndustryReportDtos);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Jogendra");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // Export the report based on the specified format
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
                throw new IllegalArgumentException("Invalid format: " + reportFormat);
        }

        return outputStream.toByteArray();
    }
}