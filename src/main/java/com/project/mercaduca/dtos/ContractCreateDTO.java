package com.project.mercaduca.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class ContractCreateDTO {
    private Long userId;
    private Double amount;
    private String kindOfPayment;
    private String paymentMethod;
    private String paymentFrequency;
}
