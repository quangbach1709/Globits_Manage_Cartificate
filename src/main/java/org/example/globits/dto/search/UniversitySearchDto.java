package org.example.globits.dto.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UniversitySearchDto {
    private String keyword;
    private Long universityId;
    private String universityName;
    private String universityAddress;
    private Integer pageIndex;
    private Integer pageSize;
}
