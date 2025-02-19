package org.example.globits.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateTypeReportDto {
    private Long typeId;
    private String typeName;
    private Long count;
}
