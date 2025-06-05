package com.project.mercaduca.dtos;

import lombok.Data;

import java.util.List;

@Data
public class ProductApprovalRequestDTO {
    private List<Long> approvedProductIds;
    private List<Long> rejectedProductIds;
    private String remark;
}
