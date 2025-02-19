package org.example.globits.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.example.globits.domain.CertificateType;
import org.example.globits.dto.CertificateTypeDto;
import org.example.globits.dto.search.CertificateTypeSearchDto;
import org.example.globits.repository.CertificateTypeRepository;
import org.example.globits.service.CertificateTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class CertificateTypeServiceImpl implements CertificateTypeService {

    @Autowired
    private CertificateTypeRepository certificateTypeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<CertificateTypeDto> getAllCertificateTypes(Pageable pageable) {
        return certificateTypeRepository.findAll(pageable).map(this::convertToDto);
    }

    @Override
    public Page<CertificateTypeDto> searchByDto(CertificateTypeSearchDto dto) {
        if (dto == null) {
            return Page.empty();
        }

        int pageIndex = dto.getPageIndex() != null ? dto.getPageIndex() : 0;
        int pageSize = dto.getPageSize() != null ? dto.getPageSize() : 10;

        if (pageSize > 0) {
            pageIndex = Math.max(0, pageIndex - 1);
        }

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT new org.example.globits.dto.CertificateTypeDto(");
        sql.append("ct.id, ct.name, ct.description, ct.field) ");
        sql.append("FROM CertificateType ct WHERE 1=1 ");

        String whereClause = buildWhereClause(dto);
        String orderBy = " ORDER BY ct.id DESC";
        String countSql = "SELECT COUNT(ct.id) FROM CertificateType ct WHERE 1=1 " + whereClause;

        TypedQuery<CertificateTypeDto> query = entityManager.createQuery(
                sql.toString() + whereClause + orderBy,
                CertificateTypeDto.class);
        TypedQuery<Long> countQuery = entityManager.createQuery(countSql, Long.class);

        // Change these lines to match the method signature
        setQueryParameters(query, dto);
        setQueryParameters(countQuery, dto);

        query.setFirstResult(pageIndex * pageSize);
        query.setMaxResults(pageSize);

        List<CertificateTypeDto> results = query.getResultList();
        Long total = countQuery.getSingleResult();

        return new PageImpl<>(results, PageRequest.of(pageIndex, pageSize), total);
    }

    private String buildWhereClause(CertificateTypeSearchDto dto) {
        StringBuilder whereClause = new StringBuilder();

        if (StringUtils.hasText(dto.getKeyword())) {
            whereClause.append(" AND (ct.name LIKE :keyword")
                    .append(" OR ct.description LIKE :keyword")
                    .append(" OR ct.field LIKE :keyword)");
        }
        if (dto.getTypeId() != null) {
            whereClause.append(" AND ct.id = :typeId");
        }
        if (StringUtils.hasText(dto.getTypeName())) {
            whereClause.append(" AND ct.name LIKE :typeName");
        }
        if (StringUtils.hasText(dto.getDescription())) {
            whereClause.append(" AND ct.description LIKE :description");
        }
        if (StringUtils.hasText(dto.getField())) {
            whereClause.append(" AND ct.field LIKE :field");
        }

        return whereClause.toString();
    }

    // Change the method signature to match the calling order
    private void setQueryParameters(Query query, CertificateTypeSearchDto dto) {
        if (StringUtils.hasText(dto.getKeyword())) {
            query.setParameter("keyword", "%" + dto.getKeyword() + "%");
        }
        if (dto.getTypeId() != null) {
            query.setParameter("typeId", dto.getTypeId());
        }
        if (StringUtils.hasText(dto.getTypeName())) {
            query.setParameter("typeName", "%" + dto.getTypeName() + "%");
        }
        if (StringUtils.hasText(dto.getDescription())) {
            query.setParameter("description", "%" + dto.getDescription() + "%");
        }
        if (StringUtils.hasText(dto.getField())) {
            query.setParameter("field", "%" + dto.getField() + "%");
        }
    }

    @Override
    public CertificateTypeDto getCertificateTypeById(Long id) {
        return certificateTypeRepository.findById(id).map(this::convertToDto).orElse(null);
    }

    @Override
    public CertificateTypeDto saveCertificateType(CertificateTypeDto certificateType) {
        if (certificateType.getId() == null) {
            return convertToDto(certificateTypeRepository.save(convertToEntity(certificateType)));
        } else {
            return convertToDto(certificateTypeRepository.save(convertToEntity(certificateType)));
        }
    }

    @Override
    public void deleteCertificateTypeById(Long id) {
        certificateTypeRepository.deleteById(id);
    }

    private CertificateTypeDto convertToDto(CertificateType certificateType) {
        CertificateTypeDto dto = new CertificateTypeDto();
        dto.setId(certificateType.getId());
        dto.setName(certificateType.getName());
        dto.setDescription(certificateType.getDescription());
        dto.setField(certificateType.getField());
        return dto;
    }

    private CertificateType convertToEntity(CertificateTypeDto dto) {
        CertificateType certificateType = new CertificateType();
        certificateType.setId(dto.getId());
        certificateType.setName(dto.getName());
        certificateType.setDescription(dto.getDescription());
        certificateType.setField(dto.getField());
        return certificateType;
    }

}
