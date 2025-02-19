package org.example.globits.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.example.globits.domain.University;
import org.example.globits.dto.UniversityDto;
import org.example.globits.dto.search.UniversitySearchDto;
import org.example.globits.repository.UniversityRepository;
import org.example.globits.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class UniversityServiceImpl implements UniversityService {
    @Autowired
    private UniversityRepository universityRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<UniversityDto> getAllUniversities(Pageable pageable) {
        return universityRepository.findAll(pageable).map(this::convertToDto);
    }

    @Override
    public Page<UniversityDto> searchByDto(UniversitySearchDto dto) {
        if (dto == null) {
            return Page.empty();
        }

        int pageIndex = dto.getPageIndex() != null ? dto.getPageIndex() : 0;
        int pageSize = dto.getPageSize() != null ? dto.getPageSize() : 10;

        if (pageSize > 0) {
            pageIndex = Math.max(0, pageIndex - 1);
        }

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT new org.example.globits.dto.UniversityDto(");
        sql.append("u.id, u.name, u.address) ");
        sql.append("FROM University u WHERE 1=1 ");

        String whereClause = buildWhereClause(dto);
        String orderBy = " ORDER BY u.id DESC";
        String countSql = "SELECT COUNT(u.id) FROM University u WHERE 1=1 " + whereClause;

        TypedQuery<UniversityDto> query = entityManager.createQuery(
                sql.toString() + whereClause + orderBy,
                UniversityDto.class);
        TypedQuery<Long> countQuery = entityManager.createQuery(countSql, Long.class);

        setQueryParameters(dto, query);
        setQueryParameters(dto, countQuery);

        query.setFirstResult(pageIndex * pageSize);
        query.setMaxResults(pageSize);

        List<UniversityDto> results = query.getResultList();
        Long total = countQuery.getSingleResult();

        return new PageImpl<>(results, PageRequest.of(pageIndex, pageSize), total);
    }

    @Override
    public UniversityDto getUniversityById(Long id) {
        return universityRepository.findById(id).map(this::convertToDto).orElse(null);
    }

    @Override
    public UniversityDto saveUniversity(UniversityDto university) {
        if (university.getId() == null) {
            return convertToDto(universityRepository.save(convertToEntity(university)));
        } else {
            return convertToDto(universityRepository.save(convertToEntity(university)));
        }
    }

    @Override
    public void deleteUniversityById(Long id) {
        universityRepository.deleteById(id);
    }

    private UniversityDto convertToDto(University university) {
        UniversityDto dto = new UniversityDto();
        dto.setId(university.getId());
        dto.setName(university.getName());
        dto.setAddress(university.getAddress());

        return dto;
    }

    private University convertToEntity(UniversityDto dto) {
        University university = new University();
        university.setId(dto.getId());
        university.setName(dto.getName());
        university.setAddress(dto.getAddress());

        return university;
    }

    private String buildWhereClause(UniversitySearchDto dto) {
        StringBuilder whereClause = new StringBuilder();

        if (StringUtils.hasText(dto.getKeyword())) {
            whereClause.append(" AND (u.name LIKE :keyword OR u.address LIKE :keyword)");
        }
        if (dto.getUniversityId() != null) {
            whereClause.append(" AND u.id = :universityId");
        }
        if (StringUtils.hasText(dto.getUniversityName())) {
            whereClause.append(" AND u.name LIKE :universityName");
        }
        if (StringUtils.hasText(dto.getUniversityAddress())) {
            whereClause.append(" AND u.address LIKE :universityAddress");
        }

        return whereClause.toString();
    }

    private void setQueryParameters(UniversitySearchDto dto, Query query) {
        if (StringUtils.hasText(dto.getKeyword())) {
            query.setParameter("keyword", "%" + dto.getKeyword() + "%");
        }
        if (dto.getUniversityId() != null) {
            query.setParameter("universityId", dto.getUniversityId());
        }
        if (StringUtils.hasText(dto.getUniversityName())) {
            query.setParameter("universityName", "%" + dto.getUniversityName() + "%");
        }
        if (StringUtils.hasText(dto.getUniversityAddress())) {
            query.setParameter("universityAddress", "%" + dto.getUniversityAddress() + "%");
        }
    }
}
