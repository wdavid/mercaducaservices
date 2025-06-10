package com.project.mercaduca.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class InventoryMovementCreateDTO {

    @NotNull(message = "El tipo es obligatorio")
    @Pattern(regexp = "E|S", message = "El tipo debe ser 'E' (Entry) o 'S' (Exit)")
    private String type;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor o igual a 1")
    private Integer quantity;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate date;

    @NotNull(message = "El ID del producto es obligatorio")
    private Long productId;
}
