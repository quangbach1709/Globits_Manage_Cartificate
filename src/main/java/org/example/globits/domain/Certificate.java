package org.example.globits.domain;

import com.fasterxml.jackson.databind.annotation.EnumNaming;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Data
@NoArgsConstructor
@Table(name = "certificate")
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String certificateNumber;
    private LocalDate issueDate;
    private String grade;
    private String status;

    @ManyToOne
    @JoinColumn(name = "university_id")
    private University university;

//    @ManyToMany
//    @JoinTable(
//            name = "certificate_student",
//            joinColumns = @JoinColumn(name = "certificate_id"),
//            inverseJoinColumns = @JoinColumn(name = "student_id")
//    )
//    private Set<Student> students = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "certificate_type_id")
    private CertificateType certificateType;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
}
