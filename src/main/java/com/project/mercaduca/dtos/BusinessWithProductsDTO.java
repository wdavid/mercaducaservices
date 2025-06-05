package com.project.mercaduca.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class BusinessWithProductsDTO {
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
    private List<ProductResponseDTO> approvedProducts;
}