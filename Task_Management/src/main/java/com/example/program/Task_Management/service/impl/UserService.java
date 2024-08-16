package com.example.program.Task_Management.service.impl;

import com.example.program.Task_Management.claass.NewPassword;
import com.example.program.Task_Management.dto.UserDTO;
import com.example.program.Task_Management.entity.UserEntity;
import com.example.program.Task_Management.security.RegistrationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

    boolean setPassword(NewPassword newPassword);

    UserDTO getUser();

    UserEntity getUser(String email);
    ResponseEntity<String> registerUser(RegistrationRequest request);


}
