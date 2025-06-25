package com.project.mercaduca.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BusinessRequestResponseDTO {
    private Long id;
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

    private String userName;
    private String userLastName;
    private String userEmail;
    private String entrepeneurKind;
    private String userGender;
}
