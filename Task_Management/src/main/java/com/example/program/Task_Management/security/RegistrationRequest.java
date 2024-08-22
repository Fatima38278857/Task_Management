package com.example.program.Task_Management.security;


public record RegistrationRequest(String login, String password, String confirmPassword) {
}
