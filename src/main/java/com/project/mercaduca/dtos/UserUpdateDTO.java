package com.project.mercaduca.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserUpdateDTO {
    private String name;
    private String lastName;
    private String mail;
    private String faculty;
    private String major;
    private String entrepeneurKind;

}