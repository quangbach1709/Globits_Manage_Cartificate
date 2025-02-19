package org.example.globits.rest;

import org.example.globits.dto.CertificateTypeReportDto;
import org.example.globits.dto.UniversityCertificateReportDto;
import org.example.globits.dto.UniversityStudentReportDto;
import org.example.globits.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/reports")
public class RestReportController {

    @Autowired
    private ReportService reportService;

    // dua ra cac bao cao ve so luong bang cua cac truong dai hoc
    @GetMapping("/certificates-by-university")
    public ResponseEntity<Page<UniversityCertificateReportDto>> getCertificatesByUniversity(Pageable pageable) {
        return ResponseEntity.ok(reportService.getCertificatesByUniversity(pageable));
    }

    // dua ra cac bao cao ve so luong bang theo loai bang (chung chi, trung cap, cao
    // dang, dai hoc)
    @GetMapping("/certificates-by-type")
    public ResponseEntity<Page<CertificateTypeReportDto>> getCertificatesByType(Pageable pageable) {
        return ResponseEntity.ok(reportService.getCertificatesByType(pageable));
    }

    // dua ra cac bao cao ve so luong sinh vien cua cac truong dai hoc
    @GetMapping("/students-by-university")
    public ResponseEntity<Page<UniversityStudentReportDto>> getStudentsByUniversity(Pageable pageable) {
        return ResponseEntity.ok(reportService.getStudentsByUniversity(pageable));
    }

    // xuat bao cao ve so luong bang cua cac truong dai hoc ra file excel
    @GetMapping("/certificates-by-university/excel")
    public ResponseEntity<Resource> exportCertificatesByUniversityToExcel() throws IOException {
        ByteArrayInputStream stream = reportService.exportCertificatesByUniversityToExcel();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=certificates_by_university_report.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(new InputStreamResource(stream));
    }

    // xuat bao cao ve so luong bang theo loai bang ra file excel
    @GetMapping("/certificates-by-type/excel")
    public ResponseEntity<Resource> exportCertificatesByTypeToExcel() throws IOException {
        ByteArrayInputStream stream = reportService.exportCertificatesByTypeToExcel();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=certificates_by_type_report.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(new InputStreamResource(stream));
    }

    // xuat bao cao ve so luong sinh vien cua cac truong dai hoc ra file excel
    @GetMapping("/students-by-university/excel")
    public ResponseEntity<Resource> exportStudentsByuniversityToExcel() throws IOException {
        ByteArrayInputStream stream = reportService.exportStudentsByUniversityToExcel();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=students_by_university_report.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(new InputStreamResource(stream));
    }
}
