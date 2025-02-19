package org.example.globits.service;

import org.example.globits.dto.CertificateTypeReportDto;
import org.example.globits.dto.UniversityCertificateReportDto;
import org.example.globits.dto.UniversityStudentReportDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public interface ReportService {
    Page<UniversityCertificateReportDto> getCertificatesByUniversity(Pageable pageable);

    Page<CertificateTypeReportDto> getCertificatesByType(Pageable pageable);

    Page<UniversityStudentReportDto> getStudentsByUniversity(Pageable pageable);

    ByteArrayInputStream exportCertificatesByUniversityToExcel() throws IOException;

    ByteArrayInputStream exportCertificatesByTypeToExcel() throws IOException;

    ByteArrayInputStream exportStudentsByUniversityToExcel() throws IOException;
}
