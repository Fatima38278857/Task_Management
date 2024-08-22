package com.example.program.Task_Management.service.impl;

import com.example.program.Task_Management.dto.UserDTO;
import com.example.program.Task_Management.entity.UserEntity;
import com.example.program.Task_Management.enumm.Role;
import com.example.program.Task_Management.security.RegistrationRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {

    UserEntity getAuthenticatedUser();
    UserDTO getUser();
    UserEntity getUser(String email);
    ResponseEntity<String> registerUser(RegistrationRequest request);
    UserEntity findById(Long id);
}
