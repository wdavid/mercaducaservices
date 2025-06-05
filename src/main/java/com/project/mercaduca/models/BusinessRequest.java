package com.project.mercaduca.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "business_request")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Datos del negocio
    private String businessName;
    private String description;
    private String status;
    private LocalDate submissionDate;
    private LocalDate reviewDate;
    private String sector;
    private String productType;
    private String priceRange;
    private String facebook;
    private String instagram;
    private String phone;
    private String urlLogo;

    // Datos del solicitante
    private String userName;
    private String userLastName;
    private String userEmail;
    private String userGender;
    private LocalDate userBirthDate;
    private String userFaculty;
    private String userMajor;
    private String entrepeneurKind;
}