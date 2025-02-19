package org.example.globits.service;

import org.example.globits.dto.CertificateTypeDto;
import org.example.globits.dto.search.CertificateTypeSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CertificateTypeService {
    Page<CertificateTypeDto> getAllCertificateTypes(Pageable pageable);

    Page<CertificateTypeDto> searchByDto(CertificateTypeSearchDto certificateTypeSearchDto);

    CertificateTypeDto getCertificateTypeById(Long id);

    CertificateTypeDto saveCertificateType(CertificateTypeDto certificateType);

    void deleteCertificateTypeById(Long id);
}
