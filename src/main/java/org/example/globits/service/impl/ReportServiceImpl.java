package org.example.globits.service.impl;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.globits.dto.CertificateTypeReportDto;
import org.example.globits.dto.UniversityCertificateReportDto;
import org.example.globits.dto.UniversityStudentReportDto;
import org.example.globits.repository.CertificateRepository;
import org.example.globits.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private CertificateRepository certificateRepository;

    @Override
    public Page<UniversityCertificateReportDto> getCertificatesByUniversity(Pageable pageable) {
        return certificateRepository.countCertificatesByUniversity(pageable);
    }

    @Override
    public Page<CertificateTypeReportDto> getCertificatesByType(Pageable pageable) {
        return certificateRepository.countCertificatesByType(pageable);
    }

    @Override
    public Page<UniversityStudentReportDto> getStudentsByUniversity(Pageable pageable) {
        return certificateRepository.countStudentsByUniversity(pageable);
    }

    @Override
    public ByteArrayInputStream exportCertificatesByUniversityToExcel() throws IOException {
        List<UniversityCertificateReportDto> data = certificateRepository
                .countCertificatesByUniversity(PageRequest.of(0, Integer.MAX_VALUE))
                .getContent();

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Certificates By University");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("University ID");
            headerRow.createCell(1).setCellValue("University Name");
            headerRow.createCell(2).setCellValue("Total Certificates");

            // Create data rows
            int rowIdx = 1;
            for (UniversityCertificateReportDto report : data) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(report.getUniversityId());
                row.createCell(1).setCellValue(report.getUniversityName());
                row.createCell(2).setCellValue(report.getTotalCertificates());
            }

            // Auto-size columns
            for (int i = 0; i < 3; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    @Override
    public ByteArrayInputStream exportCertificatesByTypeToExcel() throws IOException {
        List<CertificateTypeReportDto> data = certificateRepository
                .countCertificatesByType(PageRequest.of(0, Integer.MAX_VALUE))
                .getContent();

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Certificates By Type");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Certificate Type ID");
            headerRow.createCell(1).setCellValue("Certificate Type Name");
            headerRow.createCell(2).setCellValue("Total Count");

            // Create data rows
            int rowIdx = 1;
            for (CertificateTypeReportDto report : data) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(report.getTypeId());
                row.createCell(1).setCellValue(report.getTypeName());
                row.createCell(2).setCellValue(report.getCount());
            }

            // Auto-size columns
            for (int i = 0; i < 3; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    @Override
    public ByteArrayInputStream exportStudentsByUniversityToExcel() throws IOException {
        List<UniversityStudentReportDto> data = certificateRepository
                .countStudentsByUniversity(PageRequest.of(0, Integer.MAX_VALUE))
                .getContent();

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Students By University");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("University ID");
            headerRow.createCell(1).setCellValue("University Name");
            headerRow.createCell(2).setCellValue("Total Students");

            // Create data rows
            int rowIdx = 1;
            for (UniversityStudentReportDto report : data) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(report.getUniversityId());
                row.createCell(1).setCellValue(report.getUniversityName());
                row.createCell(2).setCellValue(report.getTotalStudents());
            }

            // Auto-size columns
            for (int i = 0; i < 3; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}
