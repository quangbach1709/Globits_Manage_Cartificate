package org.example.globits.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.globits.domain.Certificate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateDto {
    private Long id;
    private String certificateNumber;
    private LocalDate issueDate;
    private String grade;
    private String status;
    private Long universityId;
    private String universityName;
    private Long certificateTypeId;
    private String certificateTypeName;
    private Long studentId;
    private String studentName;

}

