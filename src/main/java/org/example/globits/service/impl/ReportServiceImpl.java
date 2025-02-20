package org.example.globits.service.impl;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
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
import java.io.InputStream;
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

        try (InputStream templateIs = getClass().getResourceAsStream("/template.xlsx");
                XSSFWorkbook templateWorkbook = new XSSFWorkbook(templateIs);
                XSSFWorkbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            // Get template sheet
            Sheet templateSheet = templateWorkbook.getSheetAt(0);

            // Create data sheet
            Sheet sheet = workbook.createSheet("Certificates By University");

            // Create styles
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);
            dataStyle.setAlignment(HorizontalAlignment.CENTER);
            dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            dataStyle.setWrapText(true);

            // Add data first
            int currentRow = 0;

            // Create title row first
            Row titleRow = sheet.createRow(currentRow++);
            Cell titleCell = titleRow.createCell(0); // Start at first column
            titleCell.setCellValue("BÁO CÁO THỐNG KÊ CHỨNG CHỈ THEO TRƯỜNG");
            CellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);
            titleStyle.setFont(titleFont);
            titleCell.setCellStyle(titleStyle);

            // Merge cells for title
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2)); // Merge first row across all columns

            // Create headers
            Row headerRow = sheet.createRow(currentRow++);
            String[] headers = { "University ID", "University Name", "Total Certificates" };
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Add data rows
            for (UniversityCertificateReportDto report : data) {
                Row row = sheet.createRow(currentRow++);

                Cell cell0 = row.createCell(0);
                cell0.setCellValue(report.getUniversityId());
                cell0.setCellStyle(dataStyle);

                Cell cell1 = row.createCell(1);
                cell1.setCellValue(report.getUniversityName());
                cell1.setCellStyle(dataStyle);

                Cell cell2 = row.createCell(2);
                cell2.setCellValue(report.getTotalCertificates());
                cell2.setCellStyle(dataStyle);
            }

            // Add empty row as separator
            currentRow++;

            // Copy template content if exists
            if (templateSheet != null) {
                int templateRowCount = templateSheet.getLastRowNum();
                for (int i = 0; i <= templateRowCount; i++) {
                    Row templateRow = templateSheet.getRow(i);
                    if (templateRow != null) {
                        Row newRow = sheet.createRow(currentRow++);
                        for (Cell templateCell : templateRow) {
                            if (templateCell != null) {
                                Cell newCell = newRow.createCell(templateCell.getColumnIndex());
                                // Copy style
                                CellStyle newStyle = workbook.createCellStyle();
                                newStyle.cloneStyleFrom(templateCell.getCellStyle());
                                newCell.setCellStyle(newStyle);

                                // Copy value
                                switch (templateCell.getCellType()) {
                                    case STRING:
                                        newCell.setCellValue(templateCell.getStringCellValue());
                                        break;
                                    case NUMERIC:
                                        newCell.setCellValue(templateCell.getNumericCellValue());
                                        break;
                                    case BOOLEAN:
                                        newCell.setCellValue(templateCell.getBooleanCellValue());
                                        break;
                                    case FORMULA:
                                        newCell.setCellFormula(templateCell.getCellFormula());
                                        break;
                                    default:
                                        newCell.setCellValue("");
                                }
                            }
                        }
                    }
                }
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Set smaller width for ID column (first column)
            sheet.setColumnWidth(0, 3000); // Adjust 3000 to desired width (approximately 10 characters)

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    @Override
    public ByteArrayInputStream exportCertificatesByTypeToExcel() throws IOException {
        List<CertificateTypeReportDto> data = certificateRepository
                .countCertificatesByType(PageRequest.of(0, Integer.MAX_VALUE))
                .getContent();

        try (InputStream templateIs = getClass().getResourceAsStream("/template.xlsx");
                XSSFWorkbook templateWorkbook = new XSSFWorkbook(templateIs);
                XSSFWorkbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet templateSheet = templateWorkbook.getSheetAt(0);
            Sheet sheet = workbook.createSheet("Certificates By Type");

            // Create styles
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);
            dataStyle.setAlignment(HorizontalAlignment.CENTER);
            dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            dataStyle.setWrapText(true);

            // Add data first
            int currentRow = 0;

            // Create title row first
            Row titleRow = sheet.createRow(currentRow++);
            Cell titleCell = titleRow.createCell(0); // Start at first column
            titleCell.setCellValue("BÁO CÁO THỐNG KÊ CHỨNG CHỈ THEO LOẠI");
            CellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);
            titleStyle.setFont(titleFont);
            titleCell.setCellStyle(titleStyle);

            // Merge cells for title
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2)); // Merge first row across all columns

            // Create headers
            Row headerRow = sheet.createRow(currentRow++);
            String[] headers = { "Certificate Type ID", "Certificate Type Name", "Total Count" };
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Add data rows
            for (CertificateTypeReportDto report : data) {
                Row row = sheet.createRow(currentRow++);

                Cell cell0 = row.createCell(0);
                cell0.setCellValue(report.getTypeId());
                cell0.setCellStyle(dataStyle);

                Cell cell1 = row.createCell(1);
                cell1.setCellValue(report.getTypeName());
                cell1.setCellStyle(dataStyle);

                Cell cell2 = row.createCell(2);
                cell2.setCellValue(report.getCount());
                cell2.setCellStyle(dataStyle);
            }

            // Add empty row as separator
            currentRow++;

            // Copy template content if exists
            if (templateSheet != null) {
                int templateRowCount = templateSheet.getLastRowNum();
                for (int i = 0; i <= templateRowCount; i++) {
                    Row templateRow = templateSheet.getRow(i);
                    if (templateRow != null) {
                        Row newRow = sheet.createRow(currentRow++);
                        for (Cell templateCell : templateRow) {
                            if (templateCell != null) {
                                Cell newCell = newRow.createCell(templateCell.getColumnIndex());
                                CellStyle newStyle = workbook.createCellStyle();
                                newStyle.cloneStyleFrom(templateCell.getCellStyle());
                                newCell.setCellStyle(newStyle);

                                switch (templateCell.getCellType()) {
                                    case STRING:
                                        newCell.setCellValue(templateCell.getStringCellValue());
                                        break;
                                    case NUMERIC:
                                        newCell.setCellValue(templateCell.getNumericCellValue());
                                        break;
                                    case BOOLEAN:
                                        newCell.setCellValue(templateCell.getBooleanCellValue());
                                        break;
                                    case FORMULA:
                                        newCell.setCellFormula(templateCell.getCellFormula());
                                        break;
                                    default:
                                        newCell.setCellValue("");
                                }
                            }
                        }
                    }
                }
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Set smaller width for ID column (first column)
            sheet.setColumnWidth(0, 3000); // Adjust 3000 to desired width (approximately 10 characters)

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    @Override
    public ByteArrayInputStream exportStudentsByUniversityToExcel() throws IOException {
        List<UniversityStudentReportDto> data = certificateRepository
                .countStudentsByUniversity(PageRequest.of(0, Integer.MAX_VALUE))
                .getContent();

        try (InputStream templateIs = getClass().getResourceAsStream("/template.xlsx");
                XSSFWorkbook templateWorkbook = new XSSFWorkbook(templateIs);
                XSSFWorkbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet templateSheet = templateWorkbook.getSheetAt(0);
            Sheet sheet = workbook.createSheet("Students By University");

            // Create styles
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);
            dataStyle.setAlignment(HorizontalAlignment.CENTER);
            dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            dataStyle.setWrapText(true);

            // Add data first
            int currentRow = 0;

            // Create title row first
            Row titleRow = sheet.createRow(currentRow++);
            Cell titleCell = titleRow.createCell(0); // Start at first column
            titleCell.setCellValue("BÁO CÁO THỐNG KÊ SINH VIÊN THEO TRƯỜNG");
            CellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);
            titleStyle.setFont(titleFont);
            titleCell.setCellStyle(titleStyle);

            // Merge cells for title
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2)); // Merge first row across all columns

            // Create headers
            Row headerRow = sheet.createRow(currentRow++);
            String[] headers = { "University ID", "University Name", "Total Students" };
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Add data rows
            for (UniversityStudentReportDto report : data) {
                Row row = sheet.createRow(currentRow++);

                Cell cell0 = row.createCell(0);
                cell0.setCellValue(report.getUniversityId());
                cell0.setCellStyle(dataStyle);

                Cell cell1 = row.createCell(1);
                cell1.setCellValue(report.getUniversityName());
                cell1.setCellStyle(dataStyle);

                Cell cell2 = row.createCell(2);
                cell2.setCellValue(report.getTotalStudents());
                cell2.setCellStyle(dataStyle);
            }

            // Add empty row as separator
            currentRow++;

            // Copy template content if exists
            if (templateSheet != null) {
                int templateRowCount = templateSheet.getLastRowNum();
                for (int i = 0; i <= templateRowCount; i++) {
                    Row templateRow = templateSheet.getRow(i);
                    if (templateRow != null) {
                        Row newRow = sheet.createRow(currentRow++);
                        for (Cell templateCell : templateRow) {
                            if (templateCell != null) {
                                Cell newCell = newRow.createCell(templateCell.getColumnIndex());
                                CellStyle newStyle = workbook.createCellStyle();
                                newStyle.cloneStyleFrom(templateCell.getCellStyle());
                                newCell.setCellStyle(newStyle);

                                switch (templateCell.getCellType()) {
                                    case STRING:
                                        newCell.setCellValue(templateCell.getStringCellValue());
                                        break;
                                    case NUMERIC:
                                        newCell.setCellValue(templateCell.getNumericCellValue());
                                        break;
                                    case BOOLEAN:
                                        newCell.setCellValue(templateCell.getBooleanCellValue());
                                        break;
                                    case FORMULA:
                                        newCell.setCellFormula(templateCell.getCellFormula());
                                        break;
                                    default:
                                        newCell.setCellValue("");
                                }
                            }
                        }
                    }
                }
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Set smaller width for ID column (first column)
            sheet.setColumnWidth(0, 3000); // Adjust 3000 to desired width (approximately 10 characters)

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}
