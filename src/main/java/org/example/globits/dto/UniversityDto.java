package org.example.globits.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.globits.domain.University;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UniversityDto {
    private Long id;
    private String name;
    private String address;
}


