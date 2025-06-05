package com.project.mercaduca.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {
    private Long id;
    private String name;
    private String description;
    private Integer stock;
    private String status;
    private String urlImage;
    private String userName;
    private String categoryName;
    private ProductPriceResponseDTO price;
}

