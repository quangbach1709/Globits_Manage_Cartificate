package org.example.globits.service;

import org.example.globits.dto.CertificateDto;
import org.example.globits.dto.search.CertificateSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CertificateService {
    Page<CertificateDto> getAllCertificates(Pageable pageable);

    Page<CertificateDto> searchByDto(CertificateSearchDto certificateSearchDto);

    CertificateDto getCertificateById(Long id);

    CertificateDto saveCertificate(CertificateDto certificate);

    void deleteCertificateById(Long id);

}
