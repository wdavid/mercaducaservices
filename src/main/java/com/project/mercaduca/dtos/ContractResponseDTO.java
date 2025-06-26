package com.project.mercaduca.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class ContractResponseDTO {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private Boolean renewalRequested;
    private LocalDate nextPaymentDate;
    private String paymentFrequency;
    private String userName;
    private String businessName;
}
