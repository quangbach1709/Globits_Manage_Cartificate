package org.example.globits.repository;

import org.example.globits.domain.Certificate;
import org.example.globits.dto.CertificateTypeReportDto;
import org.example.globits.dto.UniversityCertificateReportDto;
import org.example.globits.dto.UniversityStudentReportDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    // Thống kê tổng số văn bằng theo trường
    @Query("SELECT new org.example.globits.dto.UniversityCertificateReportDto(c.university.id, c.university.name, COUNT(c.id)) " +
            "FROM Certificate c GROUP BY c.university.id, c.university.name")
    Page<UniversityCertificateReportDto> countCertificatesByUniversity(Pageable pageable);

    // Thống kê loại văn bằng được phát ra
    @Query("SELECT new org.example.globits.dto.CertificateTypeReportDto(c.certificateType.id, c.certificateType.name, COUNT(c)) " +
            "FROM Certificate c GROUP BY c.certificateType.id, c.certificateType.name ORDER BY COUNT(c) DESC")
    Page<CertificateTypeReportDto> countCertificatesByType(Pageable pageable);

    // Thống kê số lượng sinh viên được phát bằng theo từng trường
    @Query("SELECT new org.example.globits.dto.UniversityStudentReportDto(c.university.id, c.university.name, COUNT(DISTINCT c.student.id)) " +
            "FROM Certificate c GROUP BY c.university.id, c.university.name")
    Page<UniversityStudentReportDto> countStudentsByUniversity(Pageable pageable);
}