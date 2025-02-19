package org.example.globits.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.globits.domain.CertificateType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateTypeDto {
    private Long id;
    private String name;
    private String description;
    private String field;
}


