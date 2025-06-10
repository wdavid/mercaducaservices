package com.project.mercaduca.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class ContractRequestDTO {
    private String businessName;
    private String phone;


    private String ownerName;
    private String ownerLastName;
    private String ownerMail;

    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private String paymentMethod;
    private String kindOfPayment;
    private LocalDate nextPaymentDate;
}
