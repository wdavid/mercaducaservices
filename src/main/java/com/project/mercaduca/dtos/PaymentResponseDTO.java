package com.project.mercaduca.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class PaymentResponseDTO {
    private Long id;
    private LocalDate date;
    private Double amount;
    private String status;
    private String remarks;
    private String userName; // o userId
}
