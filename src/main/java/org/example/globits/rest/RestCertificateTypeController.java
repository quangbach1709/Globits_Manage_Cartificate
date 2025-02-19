package org.example.globits.rest;

import org.example.globits.dto.CertificateTypeDto;
import org.example.globits.dto.search.CertificateTypeSearchDto;
import org.example.globits.service.CertificateTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/certificate-types")
public class RestCertificateTypeController {
    @Autowired
    private CertificateTypeService certificateTypeService;

    @GetMapping
    public ResponseEntity<Page<CertificateTypeDto>> getAllCertificateTypes(Pageable pageable) {
        return ResponseEntity.ok(certificateTypeService.getAllCertificateTypes(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificateTypeDto> getCertificateTypeById(@PathVariable Long id) {
        return ResponseEntity.ok(certificateTypeService.getCertificateTypeById(id));
    }

    @PostMapping
    public ResponseEntity<CertificateTypeDto> createCertificateType(
            @RequestBody CertificateTypeDto certificateTypeDto) {
        return new ResponseEntity<>(certificateTypeService.saveCertificateType(certificateTypeDto), HttpStatus.CREATED);
    }

    @PostMapping("/search")
    public ResponseEntity<Page<CertificateTypeDto>> searchCertificatesType(@RequestBody CertificateTypeSearchDto certificateTypeSearchDto) {
        Page<CertificateTypeDto> certificatesType = certificateTypeService.searchByDto(certificateTypeSearchDto);
        return ResponseEntity.ok(certificatesType);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CertificateTypeDto> updateCertificateType(@PathVariable Long id,
                                                                    @RequestBody CertificateTypeDto certificateTypeDto) {
        certificateTypeDto.setId(id);
        return ResponseEntity.ok(certificateTypeService.saveCertificateType(certificateTypeDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCertificateType(@PathVariable Long id) {
        certificateTypeService.deleteCertificateTypeById(id);
        return ResponseEntity.noContent().build();
    }
}
