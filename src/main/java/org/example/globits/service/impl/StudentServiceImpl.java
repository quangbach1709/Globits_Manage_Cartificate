package org.example.globits.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.example.globits.domain.Student;
import org.example.globits.dto.StudentDto;
import org.example.globits.dto.search.StudentSearchDto;
import org.example.globits.repository.StudentRepository;
import org.example.globits.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<StudentDto> getAllStudents(Pageable pageable) {
        return studentRepository.findAll(pageable).map(this::convertToDto);
    }

    @Override
    public StudentDto getStudentById(Long id) {
        return studentRepository.findById(id).map(this::convertToDto).orElse(null);
    }

    @Override
    public StudentDto saveStudent(StudentDto student) {
        if (student.getId() == null) {
            return convertToDto(studentRepository.save(convertToEntity(student)));
        } else {
            return convertToDto(studentRepository.save(convertToEntity(student)));
        }
    }

    @Override
    public void deleteStudentById(Long id) {
        studentRepository.deleteById(id);
    }

    @Override
    public Page<StudentDto> searchByDto(StudentSearchDto dto) {
        if (dto == null) {
            return Page.empty();
        }

        int pageIndex = dto.getPageIndex() != null ? dto.getPageIndex() : 0;
        int pageSize = dto.getPageSize() != null ? dto.getPageSize() : 10;

        if (pageSize > 0) {
            pageIndex = Math.max(0, pageIndex - 1);
        }

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT new org.example.globits.dto.StudentDto(");
        sql.append("s.id, s.name, s.address, s.studentCode) ");
        sql.append("FROM Student s WHERE 1=1 ");

        String whereClause = buildWhereClause(dto);
        String orderBy = " ORDER BY s.id DESC";
        String countSql = "SELECT COUNT(s.id) FROM Student s WHERE 1=1 " + whereClause;

        TypedQuery<StudentDto> query = entityManager.createQuery(
                sql.toString() + whereClause + orderBy,
                StudentDto.class);
        TypedQuery<Long> countQuery = entityManager.createQuery(countSql, Long.class);

        setQueryParameters(dto, query);
        setQueryParameters(dto, countQuery);

        query.setFirstResult(pageIndex * pageSize);
        query.setMaxResults(pageSize);

        List<StudentDto> results = query.getResultList();
        Long total = countQuery.getSingleResult();

        return new PageImpl<>(results, PageRequest.of(pageIndex, pageSize), total);
    }

    private String buildWhereClause(StudentSearchDto dto) {
        StringBuilder whereClause = new StringBuilder();

        if (StringUtils.hasText(dto.getKeyword())) {
            whereClause.append(" AND (s.name LIKE :keyword OR s.studentCode LIKE :keyword)");
        }
        if (dto.getStudentId() != null) {
            whereClause.append(" AND s.id = :studentId");
        }
        if (StringUtils.hasText(dto.getStudentName())) {
            whereClause.append(" AND s.name LIKE :studentName");
        }
        if (StringUtils.hasText(dto.getStudentCode())) {
            whereClause.append(" AND s.studentCode = :studentCode");
        }
        if (StringUtils.hasText(dto.getStudentAddress())) {
            whereClause.append(" AND s.address LIKE :studentAddress");
        }

        return whereClause.toString();
    }

    private void setQueryParameters(StudentSearchDto dto, Query query) {
        if (StringUtils.hasText(dto.getKeyword())) {
            query.setParameter("keyword", "%" + dto.getKeyword() + "%");
        }
        if (dto.getStudentId() != null) {
            query.setParameter("studentId", dto.getStudentId());
        }
        if (StringUtils.hasText(dto.getStudentName())) {
            query.setParameter("studentName", "%" + dto.getStudentName() + "%");
        }
        if (StringUtils.hasText(dto.getStudentCode())) {
            query.setParameter("studentCode", dto.getStudentCode());
        }
        if (StringUtils.hasText(dto.getStudentAddress())) {
            query.setParameter("studentAddress", "%" + dto.getStudentAddress() + "%");
        }
    }

    private StudentDto convertToDto(Student student) {
        StudentDto dto = new StudentDto();
        dto.setId(student.getId());
        dto.setName(student.getName());
        dto.setAddress(student.getAddress());
        dto.setCode(student.getStudentCode());
        return dto;
    }

    private Student convertToEntity(StudentDto dto) {
        Student student = new Student();
        student.setId(dto.getId());
        student.setName(dto.getName());
        student.setAddress(dto.getAddress());
        student.setStudentCode(dto.getCode());
        return student;
    }
}
