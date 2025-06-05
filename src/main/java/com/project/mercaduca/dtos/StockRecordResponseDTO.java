package com.project.mercaduca.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class StockRecordResponseDTO {
    private Long id;
    private LocalDate date;
    private Long productId;
}
