package org.example.globits.service;

import org.example.globits.dto.StudentDto;
import org.example.globits.dto.search.StudentSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentService {
    Page<StudentDto> getAllStudents(Pageable pageable);

    Page<StudentDto> searchByDto(StudentSearchDto studentSearchDto);

    StudentDto getStudentById(Long id);

    StudentDto saveStudent(StudentDto student);

    void deleteStudentById(Long id);
}
