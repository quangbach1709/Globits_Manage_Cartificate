package org.example.globits.service;

import org.example.globits.domain.University;
import org.example.globits.dto.UniversityDto;
import org.example.globits.dto.search.UniversitySearchDto;
import org.example.globits.repository.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


public interface UniversityService  {

    Page<UniversityDto> getAllUniversities(Pageable pageable);

    Page<UniversityDto> searchByDto(UniversitySearchDto universitySearchDto);

    UniversityDto getUniversityById(Long id);

    UniversityDto saveUniversity(UniversityDto university);

    void deleteUniversityById(Long id);


}
