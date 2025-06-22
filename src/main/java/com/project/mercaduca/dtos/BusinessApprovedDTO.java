package com.project.mercaduca.dtos;

import com.project.mercaduca.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessApprovedDTO {
    private Long id;
    private String urlLogo;
    private String businessName;
    private String ownerFullName;
    private String ownerEmail;
    private String major;
    private String faculty;
}