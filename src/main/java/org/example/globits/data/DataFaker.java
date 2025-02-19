package org.example.globits.data;

import com.github.javafaker.Faker;
import org.example.globits.domain.*;
import org.example.globits.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Configuration
public class DataFaker {
    private final Faker faker = new Faker(new Locale("en"));

    @Bean
    CommandLineRunner fakerData(
            UniversityRepository universityRepository,
            StudentRepository studentRepository,
            CertificateTypeRepository certificateTypeRepository,
            CertificateRepository certificateRepository) {
        return args -> {
            // Generate Universities
            List<University> universities = new ArrayList<>();
            if (universityRepository.count() == 0) {
                for (int i = 0; i < 10; i++) {
                    University university = new University();
                    university.setName(faker.university().name());
                    university.setAddress(faker.address().fullAddress());
                    universities.add(university);
                }
                universities = universityRepository.saveAll(universities);
                System.out.println("Generated 10 universities");
            }

            // Generate Students
            List<Student> students = new ArrayList<>();
            if (studentRepository.count() == 0) {
                for (int i = 0; i < 10; i++) {
                    Student student = new Student();
                    student.setName(faker.name().fullName());
                    student.setAddress(faker.address().fullAddress());
                    student.setStudentCode("ST" + faker.number().numberBetween(1000, 9999));
                    students.add(student);
                }
                students = studentRepository.saveAll(students);
                System.out.println("Generated 10 students");
            }

            // Generate Certificate Types
            List<CertificateType> certificateTypes = new ArrayList<>();
            if (certificateTypeRepository.count() == 0) {
                String[] fields = { "Engineering", "Medicine", "Business", "Arts", "Science" };
                String[] types = { "Bachelor", "Master", "PhD", "Diploma", "Certificate" };

                for (int i = 0; i < 10; i++) {
                    CertificateType type = new CertificateType();
                    type.setName(types[i % types.length] + " in " + fields[i % fields.length]);
                    type.setDescription("Description for " + type.getName());
                    type.setField(fields[i % fields.length]);
                    certificateTypes.add(type);
                }
                certificateTypes = certificateTypeRepository.saveAll(certificateTypes);
                System.out.println("Generated 10 certificate types");
            }

            // Generate Certificates
            if (certificateRepository.count() == 0) {
                List<Certificate> certificates = new ArrayList<>();
                universities = universityRepository.findAll();
                students = studentRepository.findAll();
                certificateTypes = certificateTypeRepository.findAll();

                for (int i = 0; i < 10; i++) {
                    Certificate certificate = new Certificate();
                    certificate.setCertificateNumber("CERT-" + faker.number().numberBetween(10000, 99999));
                    certificate.setIssueDate(
                            convertToLocalDate(faker.date().past(365, java.util.concurrent.TimeUnit.DAYS)));
                    certificate.setGrade(generateRandomGrade());
                    certificate.setStatus("Active");

                    // Random assignments
                    certificate.setUniversity(universities.get(faker.number().numberBetween(0, universities.size())));
                    certificate.setStudent(students.get(faker.number().numberBetween(0, students.size())));
                    certificate.setCertificateType(
                            certificateTypes.get(faker.number().numberBetween(0, certificateTypes.size())));

                    certificates.add(certificate);
                }
                certificateRepository.saveAll(certificates);
                System.out.println("Generated 10 certificates");
            }
        };
    }

    private LocalDate convertToLocalDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private String generateRandomGrade() {
        String[] grades = { "Excellent", "Very Good", "Good", "Pass" };
        return grades[faker.number().numberBetween(0, grades.length)];
    }
}