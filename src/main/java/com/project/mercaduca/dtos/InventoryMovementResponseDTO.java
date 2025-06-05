package com.project.mercaduca.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class InventoryMovementResponseDTO {
    private Long id;
    private String type;
    private Integer quantity;
    private LocalDate date;
    private Long productId;
}
