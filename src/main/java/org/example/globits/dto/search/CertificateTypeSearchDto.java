package org.example.globits.dto.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateTypeSearchDto {
    private String keyword;
    private Long typeId;
    private String typeName;
    private String description;
    private String field;
    private Integer pageIndex;
    private Integer pageSize;
}
