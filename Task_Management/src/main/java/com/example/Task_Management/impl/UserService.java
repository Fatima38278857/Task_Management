package com.example.Task_Management.impl;



import com.example.Task_Management.dto.UserDTO;
import com.example.Task_Management.entity.UserEntity;
import com.example.Task_Management.security.RegistrationRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {

    UserEntity getAuthenticatedUser();
    UserDTO getUser();
    UserEntity getUser(String email);
    ResponseEntity<String> registerUser(RegistrationRequest request);
    UserEntity findById(Long id);
   // public UserEntity addUser(UserDTO userDTO);
}
