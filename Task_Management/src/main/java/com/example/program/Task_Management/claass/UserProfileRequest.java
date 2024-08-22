package com.example.program.Task_Management.claass;

import com.example.program.Task_Management.enumm.Role;
import lombok.Data;

@Data
public class UserProfileRequest{
    private String email;
    private Role role;

    public UserProfileRequest() {
    }

    public UserProfileRequest( String email, Role role) {
        this.email = email;
        this.role = role;
    }
}
