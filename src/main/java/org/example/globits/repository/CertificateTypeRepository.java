package org.example.globits.repository;

import org.example.globits.domain.CertificateType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificateTypeRepository extends JpaRepository<CertificateType, Long> {
}
