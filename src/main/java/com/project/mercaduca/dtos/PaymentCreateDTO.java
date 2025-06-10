package com.project.mercaduca.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PaymentCreateDTO {
    private Long userId;
    private Double amount;
    private String paymentMethod;
    private String kindOfPayment;
}
