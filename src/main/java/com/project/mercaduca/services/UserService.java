package com.project.mercaduca.services;

import com.project.mercaduca.dtos.UserResponseDTO;
import com.project.mercaduca.dtos.UserUpdateDTO;
import com.project.mercaduca.models.User;
import com.project.mercaduca.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void updateProfile(UserUpdateDTO userUpdateDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByMail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setName(userUpdateDTO.getName());
        user.setLastName(userUpdateDTO.getLastName());
        user.setMail(userUpdateDTO.getMail());
        user.setFaculty(userUpdateDTO.getFaculty());
        user.setMajor(userUpdateDTO.getMajor());
        user.setEntrepeneurKind(userUpdateDTO.getEntrepeneurKind());

        userRepository.save(user);
    }

    public UserResponseDTO getCurrentUserProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByMail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setLastName(user.getLastName());
        dto.setMail(user.getMail());
        dto.setGender(user.getGender());
        dto.setFaculty(user.getFaculty());
        dto.setMajor(user.getMajor());
        dto.setEntrepeneurKind(user.getEntrepeneurKind());

        return dto;
    }


}
