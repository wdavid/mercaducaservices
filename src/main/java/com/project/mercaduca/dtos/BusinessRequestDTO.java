package com.project.mercaduca.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BusinessRequestDTO {
    private Long id;
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
