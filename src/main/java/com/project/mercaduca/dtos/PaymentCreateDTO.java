package com.project.mercaduca.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class PaymentCreateDTO {
    private Long userId;
    private Double amount;
    private String paymentMethod;
    private String kindOfPayment;
}
