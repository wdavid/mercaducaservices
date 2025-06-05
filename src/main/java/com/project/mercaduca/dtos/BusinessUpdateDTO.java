package com.project.mercaduca.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class BusinessUpdateDTO {
    private String businessName;
    private String description;
    private String sector;
    private String productType;
    private String priceRange;
    private String facebook;
    private String instagram;
    private String phone;
    private String urlLogo;
}
