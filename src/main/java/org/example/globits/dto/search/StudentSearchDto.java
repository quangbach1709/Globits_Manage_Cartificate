package org.example.globits.dto.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentSearchDto {
    private String keyword;
    private Long studentId;
    private String studentName;
    private String studentCode;
    private String studentAddress;
    private Integer pageIndex;
    private Integer pageSize;
}
