package com.project.mercaduca.dtos;

import com.project.mercaduca.models.BusinessRequest;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BusinessContractRequestDTO {
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
    private String userGender;
    private LocalDate userBirthDate;

    private String facultyName;
    private String majorName;

    private String entrepeneurKind;

}
