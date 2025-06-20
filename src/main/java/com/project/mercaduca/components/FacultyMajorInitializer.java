package com.project.mercaduca.components;

import com.project.mercaduca.models.Faculty;
import com.project.mercaduca.models.Major;
import com.project.mercaduca.repositories.FacultyRepository;
import com.project.mercaduca.repositories.MajorRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FacultyMajorInitializer {

    private final FacultyRepository facultyRepo;
    private final MajorRepository majorRepo;

    public FacultyMajorInitializer(FacultyRepository facultyRepo, MajorRepository majorRepo) {
        this.facultyRepo = facultyRepo;
        this.majorRepo = majorRepo;
    }

    @PostConstruct
    public void init() {
        if (facultyRepo.count() > 0) return;

        Faculty f1 = facultyRepo.save(new Faculty(null, "Arquitectura e Ingenierías", null));
        Faculty f2 = facultyRepo.save(new Faculty(null, "Ciencias Sociales y Humanidades", null));
        Faculty f3 = facultyRepo.save(new Faculty(null, "Comunicaciones y Mercadeo", null));
        Faculty f4 = facultyRepo.save(new Faculty(null, "Derecho", null));
        Faculty f5 = facultyRepo.save(new Faculty(null, "Diseño", null));
        Faculty f6 = facultyRepo.save(new Faculty(null, "Educación", null));
        Faculty f7 = facultyRepo.save(new Faculty(null, "Gestión Empresarial y Economía", null));

        majorRepo.saveAll(List.of(
                new Major(null, "Arquitectura", f1),
                new Major(null, "Ingeniería de Alimentos", f1),
                new Major(null, "Ingeniería Civil", f1),
                new Major(null, "Ingeniería Eléctrica", f1),
                new Major(null, "Ingeniería Energética", f1),
                new Major(null, "Ingeniería Industrial", f1),
                new Major(null, "Ingeniería Informática", f1),
                new Major(null, "Ingeniería Mecánica", f1),
                new Major(null, "Ingeniería Química", f1),

                new Major(null, "Licenciatura en Ciencias Sociales (semipresencial)", f2),
                new Major(null, "Licenciatura en Filosofía (semipresencial)", f2),
                new Major(null, "Licenciatura en Idioma Inglés", f2),
                new Major(null, "Licenciatura en Psicología", f2),
                new Major(null, "Licenciatura en Teología", f2),

                new Major(null, "Técnico en Marketing Digital (semipresencial)", f3),
                new Major(null, "Técnico en Producción Multimedia (semipresencial)", f3),
                new Major(null, "Licenciatura en Mercadeo (semipresencial)", f3),
                new Major(null, "Licenciatura en Comunicación Social (semipresencial)", f3),

                new Major(null, "Licenciatura en Ciencias Jurídicas", f4),

                new Major(null, "Licenciatura en Diseño (semipresencial)", f5),

                new Major(null, "Profesorado en Idioma Inglés", f6),
                new Major(null, "Profesorado en Teología", f6),

                new Major(null, "Técnico en Contaduría (semipresencial)", f7),
                new Major(null, "Licenciatura en Administración de Empresas (semipresencial)", f7),
                new Major(null, "Licenciatura en Contaduría Pública (semipresencial)", f7),
                new Major(null, "Licenciatura en Economía", f7),
                new Major(null, "Licenciatura en Finanzas (semipresencial)", f7)
        ));
    }
}
