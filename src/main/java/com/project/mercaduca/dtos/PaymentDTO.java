package com.project.mercaduca.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PaymentDTO {
    private Long id;
    private LocalDate date;
    private LocalDate expectedDate;
    private Double amount;
    private String status;
    private String paymentMethod;
    private String kindOfPayment;
}
