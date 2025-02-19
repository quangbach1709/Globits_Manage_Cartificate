package org.example.globits.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UniversityStudentReportDto {
    private Long universityId;
    private String universityName;
    private Long totalStudents;
}
