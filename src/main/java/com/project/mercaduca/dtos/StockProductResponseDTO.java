package com.project.mercaduca.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class StockProductResponseDTO {
    private Long id;
    private Integer quantity;
    private Long productId;
    private Long stockId;
}
