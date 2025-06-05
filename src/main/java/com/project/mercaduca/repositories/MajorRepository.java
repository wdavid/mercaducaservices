package com.project.mercaduca.repositories;

import com.project.mercaduca.models.Major;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MajorRepository extends JpaRepository<Major, Long> {
    Optional<Major> findByName(String name);
    List<Major> findByFacultyId(Long facultyId);
}