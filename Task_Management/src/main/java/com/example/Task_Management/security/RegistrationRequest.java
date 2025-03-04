package com.example.Task_Management.security;



public record RegistrationRequest(String login, String password, String confirmPassword) {
}
