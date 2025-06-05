package com.project.mercaduca.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserResponseDTO {
    private Long id;
    private String name;
    private String lastName;
    private String mail;
    private String gender;
    private String faculty;
    private String major;
    private String entrepeneurKind;

}