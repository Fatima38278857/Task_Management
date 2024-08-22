package com.example.program.Task_Management.service;

import com.example.program.Task_Management.dto.UserDTO;
import com.example.program.Task_Management.entity.Register;
import com.example.program.Task_Management.entity.UserEntity;
import com.example.program.Task_Management.enumm.Role;
import com.example.program.Task_Management.exception.UserNotFoundException;
import com.example.program.Task_Management.mapper.UserMapper;
import com.example.program.Task_Management.mapperr.UserMapperr;
import com.example.program.Task_Management.repository.RegisterRepository;
import com.example.program.Task_Management.repository.UserRepository;
import com.example.program.Task_Management.security.RegistrationRequest;
import com.example.program.Task_Management.service.impl.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/*

 */
@Service
public class UserImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserImpl.class);
    private final UserRepository userRepository;
    private final UserMapperr userMapperr;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RegisterRepository registerRepository;

    @Autowired
    public UserImpl(UserRepository userRepository, UserMapperr userMapperr, BCryptPasswordEncoder passwordEncoder, RegisterRepository registerRepository) {
        this.userRepository = userRepository;
        this.userMapperr = userMapperr;
        this.passwordEncoder = passwordEncoder;
        this.registerRepository = registerRepository;
    }


    @Override
    public UserEntity getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }

        String username = authentication.getName();
        return userRepository.findByRegisterUsername(username);
    }

    /*

     */
    private UserEntity currentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return getUser(((UserDetails) authentication.getPrincipal()).getUsername());
        } catch (NullPointerException е) {
            е.printStackTrace();
            log.info("Пользователь не аутентифицирован");
        }
        return null;
    }


    @Override
    public UserDTO getUser() {
        UserEntity user = currentUser();

        if (user != null) {
            return userMapperr.toDTO(user);
        }
        throw new RuntimeException("Нет авторизованного пользователя");
    }

    @Override
    public UserEntity getUser(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    /*
    Метод отвечает за регистрацию пользователя
     */
    @Override
    public ResponseEntity<String> registerUser(RegistrationRequest request) {
        String username = request.login(); // Используем правильный метод геттера
        String password = request.password();
        String confirmPassword = request.confirmPassword();

        // Проверяем на пустые значения
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя пользователя не может быть пустым");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Пароль не может быть пустым");
        }
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Пароли не совпадают");
        }
        if (userRepository.findByUsername(request.login()).isPresent()) {
            throw new IllegalArgumentException("Имя пользователя уже занято");
        }
        Register register = new Register(request.login(), passwordEncoder.encode(request.password()));
        registerRepository.save(register);

        return ResponseEntity.ok("Пользователь успешно зарегистрирован! Далее пройдите аутентификацию и создайте учетную запись");
    }

    public UserEntity findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
