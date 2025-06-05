package com.project.mercaduca.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class ProductApprovalResponseDTO {
    private Long id;
    private String status;
    private LocalDate reviewDate;
    private String remarks;
    private Long productId;
    private String productName;
}
