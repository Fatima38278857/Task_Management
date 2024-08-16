package com.example.program.Task_Management.service;

import com.example.program.Task_Management.claass.NewPassword;
import com.example.program.Task_Management.dto.UserDTO;
import com.example.program.Task_Management.entity.Register;
import com.example.program.Task_Management.entity.UserEntity;
import com.example.program.Task_Management.enumm.Role;
import com.example.program.Task_Management.mapper.UserMapper;
import com.example.program.Task_Management.repository.RegisterRepository;
import com.example.program.Task_Management.repository.UserRepository;
import com.example.program.Task_Management.security.RegistrationRequest;
import com.example.program.Task_Management.service.impl.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserImpl.class);
    private UserRepository userRepository;
    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;
    private RegisterRepository registerRepository;

    public UserImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;

    }
    private UserEntity currentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return getUser(((UserDetails) authentication.getPrincipal()).getUsername());
        } catch (NullPointerException е) {
            log.info("Пользователь не аутентифицирован");
        }
        return null;
    }

    @Override
    public boolean setPassword(NewPassword newPassword) {
        UserEntity user = currentUser();
        if (passwordEncoder.matches(newPassword.getCurrentPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword.getNewPassword()));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public UserDTO getUser() {
        UserEntity user = currentUser();
        if (user != null) {
            return userMapper.toDTO(user);
        }
        throw new RuntimeException("Нет авторизованного пользователя");
    }

    @Override
    public UserEntity getUser(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    @Override
    public ResponseEntity<String> registerUser(RegistrationRequest request) {
        if (userRepository.findByUsername(request.login()).isPresent()) {
            throw new IllegalArgumentException("Имя пользователя уже занято");
        }
        Register register = new Register(request.login(), passwordEncoder.encode(request.password()));
        registerRepository.save(register);

        return ResponseEntity.ok("Пользователь успешно зарегистрирован");
    }



}
