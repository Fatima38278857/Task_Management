package com.example.program.Task_Management.dto;

import com.example.program.Task_Management.enumm.Role;
import lombok.Data;



@Data
public class UserDTO {
    private Long id;
    private String email;
    private String username;
    private Role role;
}
