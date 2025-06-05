package com.project.mercaduca.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class UserCreateDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "El apellido es obligatorio")
    private String lastName;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "Debe proporcionar un correo electrónico válido")
    private String mail;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    @NotBlank(message = "El género es obligatorio")
    private String gender;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    private LocalDate birthDate;

    @NotBlank(message = "La facultad es obligatoria")
    private String faculty;

    @NotBlank(message = "La carrera es obligatoria")
    private String major;

    @NotNull(message = "El rol es obligatorio")
    private Long roleId;

    @NotBlank(message = "El tipo de emprendedor es obligatorio")
    private String entrepeneurKind;
}
