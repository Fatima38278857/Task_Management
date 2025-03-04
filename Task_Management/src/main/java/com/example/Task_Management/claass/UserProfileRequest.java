package com.example.Task_Management.claass;



import com.example.Task_Management.enums.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class UserProfileRequest{
    @JsonProperty("email")
    private String email;
    @JsonProperty("role")
    private Role role;

    public UserProfileRequest() {
    }

    public UserProfileRequest(String email, Role role) {
        this.email = email;
        this.role = role;
    }

}
