package com.project.mercaduca.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BusinessSummaryDTO {
    private Long id;
    private String businessName;
    private String urlLogo;
    private String description;
}
