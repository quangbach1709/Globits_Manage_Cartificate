package org.example.globits.dto.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateSearchDto {
    private String keyword;
    private Long certificateId;
    private String certificateName;
    private String certificateNumber;
    private String certificateGrade;
    private String certificateStatus;
    private LocalDate certificateDateFrom;
    private Long certificateTypeId;
    private String certificateTypeName;
    private Long universityId;
    private String universityName;
    private Long studentId;
    private String studentName;

    private Integer pageIndex;
    private Integer pageSize;

}
