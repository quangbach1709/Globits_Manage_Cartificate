package org.example.globits.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "certificate_type")
public class CertificateType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    private String description;
    private String field; // Lĩnh vực

    @OneToMany(mappedBy = "certificateType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Certificate> certificates= new HashSet<>();
}
