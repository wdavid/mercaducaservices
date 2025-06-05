package com.project.mercaduca.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CategoryCreateDTO {
    @NotNull
    private String name;

    @NotNull
    private String description;
}
