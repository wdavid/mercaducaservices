package com.project.mercaduca.controllers;

import com.project.mercaduca.dtos.FacultyResponseDTO;
import com.project.mercaduca.dtos.MajorResponseDTO;
import com.project.mercaduca.models.Faculty;
import com.project.mercaduca.models.Major;
import com.project.mercaduca.repositories.FacultyRepository;
import com.project.mercaduca.repositories.MajorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/major")
public class MajorListContoller {
    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private MajorRepository majorRepository;

    @GetMapping
    public List<FacultyResponseDTO> getAllFaculties() {
        return facultyRepository.findAll().stream()
                .map(faculty -> {
                    FacultyResponseDTO dto = new FacultyResponseDTO();
                    dto.setId(faculty.getId());
                    dto.setName(faculty.getName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/majors")
    public List<MajorResponseDTO> getMajorsByFaculty(@PathVariable Long id) {
        return majorRepository.findByFacultyId(id).stream()
                .map(major -> {
                    MajorResponseDTO dto = new MajorResponseDTO();
                    dto.setId(major.getId());
                    dto.setName(major.getName());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
