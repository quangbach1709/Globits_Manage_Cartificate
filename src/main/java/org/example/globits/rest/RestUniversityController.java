package org.example.globits.rest;

import org.example.globits.dto.UniversityDto;
import org.example.globits.dto.search.UniversitySearchDto;
import org.example.globits.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/universities")
public class RestUniversityController {
    @Autowired
    private UniversityService universityService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

    @GetMapping("/all")
    public ResponseEntity<Page<UniversityDto>> getAllUniversities(Pageable pageable) {
        return ResponseEntity.ok(universityService.getAllUniversities(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UniversityDto> getUniversityById(@PathVariable Long id) {
        return ResponseEntity.ok(universityService.getUniversityById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<UniversityDto> saveUniversity(@RequestBody UniversityDto universityDto) {
        return new ResponseEntity<>(universityService.saveUniversity(universityDto), HttpStatus.CREATED);
    }

    @PostMapping("/search")
    public ResponseEntity<Page<UniversityDto>> searchUniversity(@RequestBody UniversitySearchDto universitySearchDto) {
        Page<UniversityDto> universityDtos = universityService.searchByDto(universitySearchDto);
        return ResponseEntity.ok(universityDtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UniversityDto> updateUniversity(@PathVariable Long id, @RequestBody UniversityDto universityDto) {
        universityDto.setId(id);
        return ResponseEntity.ok(universityService.saveUniversity(universityDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUniversity(@PathVariable Long id) {
        universityService.deleteUniversityById(id);
        return ResponseEntity.noContent().build();
    }
}

