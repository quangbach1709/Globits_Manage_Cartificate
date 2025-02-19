package org.example.globits.service.impl;

import jakarta.persistence.Query;
import org.example.globits.domain.Certificate;
import org.example.globits.domain.CertificateType;
import org.example.globits.domain.Student;
import org.example.globits.domain.University;
import org.example.globits.dto.CertificateDto;
import org.example.globits.dto.search.CertificateSearchDto;
import org.example.globits.repository.CertificateRepository;
import org.example.globits.repository.CertificateTypeRepository;
import org.example.globits.repository.StudentRepository;
import org.example.globits.repository.UniversityRepository;
import org.example.globits.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Service
public class CertificateServiceImpl implements CertificateService {
    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private CertificateTypeRepository certificateTypeRepository;

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private StudentRepository studentRepository;

    @PersistenceContext
    private EntityManager entityManager; // Add this field

    @Override
    public Page<CertificateDto> getAllCertificates(Pageable pageable) {
        return certificateRepository.findAll(pageable).map(this::convertToDto);
    }

    @Override
    public Page<CertificateDto> searchByDto(CertificateSearchDto dto) {
        if (dto == null) {
            return Page.empty();
        }

        int pageIndex = dto.getPageIndex() != null ? dto.getPageIndex() : 0;
        int pageSize = dto.getPageSize() != null ? dto.getPageSize() : 10;

        if (pageSize > 0) {
            pageIndex = Math.max(0, pageIndex - 1);
        }

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT new org.example.globits.dto.CertificateDto(");
        sql.append("c.id, c.certificateNumber, c.issueDate, c.grade, c.status, ");
        sql.append("c.university.id, c.university.name, ");
        sql.append("c.certificateType.id, c.certificateType.name, ");
        sql.append("c.student.id, c.student.name) ");
        sql.append("FROM Certificate c WHERE 1=1 ");

        String whereClause = buildWhereClause(dto);
        String orderBy = " ORDER BY c.id DESC";

        String countSql = "SELECT COUNT(c.id) FROM Certificate c WHERE 1=1 " + whereClause;

        TypedQuery<CertificateDto> query = entityManager.createQuery(
                sql.toString() + whereClause + orderBy,
                CertificateDto.class);
        TypedQuery<Long> countQuery = entityManager.createQuery(countSql, Long.class);

        // Set parameters
        setQueryParameters(dto, query);
        setQueryParameters(dto, countQuery);

        // Pagination
        query.setFirstResult(pageIndex * pageSize);
        query.setMaxResults(pageSize);

        List<CertificateDto> results = query.getResultList();
        Long total = countQuery.getSingleResult();

        return new PageImpl<>(results, PageRequest.of(pageIndex, pageSize), total);
    }

    private String buildWhereClause(CertificateSearchDto dto) {
        StringBuilder whereClause = new StringBuilder();

        // Keyword search across multiple fields
        if (StringUtils.hasText(dto.getKeyword())) {
            whereClause.append(" AND (c.certificateNumber LIKE :keyword")
                    .append(" OR c.grade LIKE :keyword")
                    .append(" OR c.status LIKE :keyword")
                    .append(" OR c.university.name LIKE :keyword")
                    .append(" OR c.student.name LIKE :keyword)");
        }

        // Direct matches
        if (dto.getCertificateId() != null) {
            whereClause.append(" AND c.id = :certificateId");
        }
        if (StringUtils.hasText(dto.getCertificateNumber())) {
            whereClause.append(" AND c.certificateNumber = :certificateNumber");
        }
        if (StringUtils.hasText(dto.getCertificateGrade())) {
            whereClause.append(" AND c.grade = :grade");
        }
        if (StringUtils.hasText(dto.getCertificateStatus())) {
            whereClause.append(" AND c.status = :status");
        }

        // Date comparison
        if (dto.getCertificateDateFrom() != null) {
            whereClause.append(" AND c.issueDate >= :dateFrom");
        }

        // Related entity searches
        if (dto.getCertificateTypeId() != null) {
            whereClause.append(" AND c.certificateType.id = :typeId");
        }
        // Remove StringUtils.hasText check for Long type
        if (dto.getCertificateTypeName() != null) {
            whereClause.append(" AND c.certificateType.name = :typeName");
        }

        if (dto.getUniversityId() != null) {
            whereClause.append(" AND c.university.id = :universityId");
        }
        if (StringUtils.hasText(dto.getUniversityName())) {
            whereClause.append(" AND c.university.name LIKE :universityName");
        }

        if (dto.getStudentId() != null) {
            whereClause.append(" AND c.student.id = :studentId");
        }
        if (StringUtils.hasText(dto.getStudentName())) {
            whereClause.append(" AND c.student.name LIKE :studentName");
        }

        return whereClause.toString();
    }

    private void setQueryParameters(CertificateSearchDto dto, Query query) {
        if (StringUtils.hasText(dto.getKeyword())) {
            query.setParameter("keyword", "%" + dto.getKeyword() + "%");
        }
        if (dto.getCertificateId() != null) {
            query.setParameter("certificateId", dto.getCertificateId());
        }
        if (StringUtils.hasText(dto.getCertificateNumber())) {
            query.setParameter("certificateNumber", dto.getCertificateNumber());
        }
        if (StringUtils.hasText(dto.getCertificateGrade())) {
            query.setParameter("grade", dto.getCertificateGrade());
        }
        if (StringUtils.hasText(dto.getCertificateStatus())) {
            query.setParameter("status", dto.getCertificateStatus());
        }
        if (dto.getCertificateDateFrom() != null) {
            query.setParameter("dateFrom", dto.getCertificateDateFrom());
        }

        // Certificate Type parameters
        if (dto.getCertificateTypeId() != null) {
            query.setParameter("typeId", dto.getCertificateTypeId());
        }
        // Remove StringUtils.hasText check for Long type
        if (dto.getCertificateTypeName() != null) {
            query.setParameter("typeName", dto.getCertificateTypeName());
        }

        // University parameters
        if (dto.getUniversityId() != null) {
            query.setParameter("universityId", dto.getUniversityId());
        }
        if (StringUtils.hasText(dto.getUniversityName())) {
            query.setParameter("universityName", "%" + dto.getUniversityName() + "%");
        }

        // Student parameters
        if (dto.getStudentId() != null) {
            query.setParameter("studentId", dto.getStudentId());
        }
        if (StringUtils.hasText(dto.getStudentName())) {
            query.setParameter("studentName", "%" + dto.getStudentName() + "%");
        }
    }

    @Override
    public CertificateDto getCertificateById(Long id) {
        return certificateRepository.findById(id).map(this::convertToDto).orElse(null);
    }

    @Override
    public CertificateDto saveCertificate(CertificateDto dto) {
        Certificate certificate;
        if (dto.getId() != null) {
            // For update, fetch existing entity first
            certificate = certificateRepository.findById(dto.getId())
                    .orElse(new Certificate());
        } else {
            certificate = new Certificate();
        }

        // Update fields
        certificate.setCertificateNumber(dto.getCertificateNumber());
        certificate.setIssueDate(dto.getIssueDate());
        certificate.setGrade(dto.getGrade());
        certificate.setStatus(dto.getStatus());

        // Set relationships using IDs
        if (dto.getUniversityId() != null) {
            University university = universityRepository.findById(dto.getUniversityId())
                    .orElseThrow(() -> new RuntimeException("University not found"));
            certificate.setUniversity(university);
        }

        if (dto.getCertificateTypeId() != null) {
            CertificateType certificateType = certificateTypeRepository.findById(dto.getCertificateTypeId())
                    .orElseThrow(() -> new RuntimeException("Certificate type not found"));
            certificate.setCertificateType(certificateType);
        }

        if (dto.getStudentId() != null) {
            Student student = studentRepository.findById(dto.getStudentId())
                    .orElseThrow(() -> new RuntimeException("Student not found"));
            certificate.setStudent(student);
        }

        // Save and convert back to DTO
        certificate = certificateRepository.save(certificate);
        return convertToDto(certificate);
    }

    @Override
    public void deleteCertificateById(Long id) {
        certificateRepository.deleteById(id);
    }

    private CertificateDto convertToDto(Certificate certificate) {
        CertificateDto dto = new CertificateDto();
        dto.setId(certificate.getId());
        dto.setCertificateNumber(certificate.getCertificateNumber());
        dto.setIssueDate(certificate.getIssueDate());
        dto.setGrade(certificate.getGrade());
        dto.setStatus(certificate.getStatus());

        if (certificate.getUniversity() != null) {
            dto.setUniversityId(certificate.getUniversity().getId());
            dto.setUniversityName(certificate.getUniversity().getName());
        }

        if (certificate.getCertificateType() != null) {
            dto.setCertificateTypeId(certificate.getCertificateType().getId());
            dto.setCertificateTypeName(certificate.getCertificateType().getName());
        }

        if (certificate.getStudent() != null) {
            dto.setStudentId(certificate.getStudent().getId());
            dto.setStudentName(certificate.getStudent().getName());
        }

        return dto;
    }

    private Certificate convertToEntity(CertificateDto dto) {
        Certificate certificate = new Certificate();
        certificate.setId(dto.getId());
        certificate.setCertificateNumber(dto.getCertificateNumber());
        certificate.setIssueDate(dto.getIssueDate());
        certificate.setGrade(dto.getGrade());
        certificate.setStatus(dto.getStatus());
        University university = universityRepository.findById(dto.getUniversityId()).orElse(null);
        certificate.setUniversity(university);
        CertificateType certificateType = certificateTypeRepository.findById(dto.getCertificateTypeId()).orElse(null);
        certificate.setCertificateType(certificateType);
        Student student = studentRepository.findById(dto.getStudentId()).orElse(null);
        certificate.setStudent(student);
        return certificate;
    }
}
