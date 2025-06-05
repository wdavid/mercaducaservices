package com.project.mercaduca.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class ProductApprovalCreateDTO {

    @NotBlank(message = "El estado es obligatorio")
    private String status;

    @NotNull(message = "La fecha de revisión es obligatoria")
    private LocalDate reviewDate;

    private String remarks;

    @NotNull(message = "El ID del producto es obligatorio")
    private Long productId;
}
