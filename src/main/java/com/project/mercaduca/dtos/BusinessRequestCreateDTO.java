package com.project.mercaduca.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class BusinessRequestCreateDTO {
    @NotNull(message = "Nombre de negocio obligatorio")
    private String businessName;

    @NotNull(message = "Descripcion del negocio obligatorio")
    private String description;

    @NotNull(message = "Nombre del sector obligatorio")
    private String sector;

    @NotNull(message = "Tipo de producto obligatorio")
    private String productType;

    @NotNull(message = "Rango de precio obligatorio")
    private String priceRange;

    private String facebook;

    private String instagram;

    @NotNull(message = "Telefono obligatorio")
    private String phone;

    @NotNull(message = "Logo obligatorio")
    private String urlLogo;

    @NotNull(message = "Nombre de usuario obligatorio")
    private String userName;

    @NotNull(message = "Apellido de usuario obligatorio")
    private String userLastName;

    @NotNull(message = "Email de usuario obligatorio")
    private String userEmail;

    @NotNull(message = "Genero de usuario obligatorio")
    private String userGender;

    @NotNull(message = "Fecha de nacimiento de usuario obligatoria")
    private LocalDate userBirthDate;

    @NotNull(message = "Facultad de usuario obligatoria")
    private String userFaculty;

    @NotNull(message = "Carrera de usuario obligatoria")
    private String userMajor;

    @NotNull(message = "Tipo de emprendedor obligatorio")
    private String entrepeneurKind;
}
