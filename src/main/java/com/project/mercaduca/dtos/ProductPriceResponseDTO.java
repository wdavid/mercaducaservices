package com.project.mercaduca.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class ProductPriceResponseDTO {
    private Long id;
    private Double price;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long productId;
}
