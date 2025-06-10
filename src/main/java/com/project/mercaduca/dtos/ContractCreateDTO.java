package com.project.mercaduca.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ContractCreateDTO {
    private Long userId;
    private Double amount;
    private String kindOfPayment;
    private String paymentMethod;
    private String paymentFrequency;
}
