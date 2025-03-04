package com.example.Task_Management.dto;


import com.example.Task_Management.enums.Role;
import lombok.Data;



@Data
public class UserDTO {
    private Long id;
    private String email;
    private String username;
    private Role role;
}
