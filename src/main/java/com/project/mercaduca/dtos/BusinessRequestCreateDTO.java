package com.project.mercaduca.dtos;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class BusinessRequestCreateDTO {
    @NotNull(message = "Nombre de negocio obligatorio")
    @NotBlank(message = "Nombre de negocio no puede estar en blanco")
    private String businessName;

    @NotNull(message = "Descripcion del negocio obligatorio")
    @NotBlank(message = "Descripcion del negocio no puede estar en blanco")
    private String description;

    @NotNull(message = "Nombre del sector obligatorio")
    @NotBlank(message = "Nombre del sector no puede estar en blanco")
    private String sector;

    @NotNull(message = "Tipo de producto obligatorio")
    @NotBlank(message = "Tipo de producto no puede estar en blanco")
    private String productType;

    @NotNull(message = "Rango de precio obligatorio")
    @NotBlank(message = "Rango de precio no puede estar en blanco")
    private String priceRange;

    private String facebook;

    private String instagram;

    @NotNull(message = "Telefono obligatorio")
    @NotBlank(message = "Telefono no puede estar en blanco")
    @Pattern(regexp = "^[0-9]+$", message = "El teléfono solo debe contener números")
    @Size(min = 8, max = 15, message = "El teléfono debe tener entre 8 y 15 caracteres")
    private String phone;


    private String urlLogo;

    @NotNull(message = "Nombre de usuario obligatorio")
    @NotBlank(message = "Nombre de usuario no puede estar en blanco")
    private String userName;

    @NotNull(message = "Apellido de usuario obligatorio")
    @NotBlank(message = "Apellido de usuario no puede estar en blanco")
    private String userLastName;

    @NotBlank(message = "Email obligatorio")
    @Email(message = "Debe ser un correo válido")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@uca\\.edu\\.sv$", message = "El correo debe ser del dominio @uca.edu.sv")
    private String userEmail;

    @NotNull(message = "Genero de usuario obligatorio")
    @NotBlank(message = "Genero de usuario no puede estar en blanco")
    private String userGender;

    @NotNull(message = "Fecha de nacimiento de usuario obligatoria")
    @Past(message = "La fecha debe ser en el pasado")
    private LocalDate userBirthDate;

    @NotNull(message = "Facultad de usuario obligatoria")
    @NotBlank(message = "Facultad de usuario no puede estar en blanco")
    private String userFaculty;

    @NotNull(message = "Carrera de usuario obligatoria")
    @NotBlank(message = "Carrera de usuario no puede estar en blanco")
    private String userMajor;

    @NotNull(message = "Tipo de emprendedor obligatorio")
    @NotBlank(message = "Tipo de emprendedor no puede estar en blanco")
    private String entrepeneurKind;
}
