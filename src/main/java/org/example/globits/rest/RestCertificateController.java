package org.example.globits.rest;

import org.example.globits.dto.CertificateDto;
import org.example.globits.dto.search.CertificateSearchDto;
import org.example.globits.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/certificates")
public class RestCertificateController {
    @Autowired
    private CertificateService certificateService;

    @GetMapping
    public ResponseEntity<Page<CertificateDto>> getAllCertificates(Pageable pageable) {
        return ResponseEntity.ok(certificateService.getAllCertificates(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificateDto> getCertificateById(@PathVariable Long id) {
        return ResponseEntity.ok(certificateService.getCertificateById(id));
    }

    @PostMapping
    public ResponseEntity<CertificateDto> createCertificate(@RequestBody CertificateDto certificateDto) {
        return new ResponseEntity<>(certificateService.saveCertificate(certificateDto), HttpStatus.CREATED);
    }

    @PostMapping("/search")
    public ResponseEntity<Page<CertificateDto>> searchCertificates(@RequestBody CertificateSearchDto certificateSearchDto) {
        Page<CertificateDto> certificates = certificateService.searchByDto(certificateSearchDto);
        return ResponseEntity.ok(certificates);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CertificateDto> updateCertificate(@PathVariable Long id,
                                                            @RequestBody CertificateDto certificateDto) {
        certificateDto.setId(id);
        return ResponseEntity.ok(certificateService.saveCertificate(certificateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCertificate(@PathVariable Long id) {
        certificateService.deleteCertificateById(id);
        return ResponseEntity.noContent().build();
    }
}
