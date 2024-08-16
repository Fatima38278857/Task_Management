package com.example.program.Task_Management.claass;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class Login {

    @Schema(description = "пароль")
    private String password;
    @Schema(description = "логин")
    private String username;
}
