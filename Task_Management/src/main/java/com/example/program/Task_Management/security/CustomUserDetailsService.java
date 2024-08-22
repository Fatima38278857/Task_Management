package com.example.program.Task_Management.security;

import com.example.program.Task_Management.entity.Register;
import com.example.program.Task_Management.entity.UserEntity;
import com.example.program.Task_Management.repository.RegisterRepository;
import com.example.program.Task_Management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service

public class CustomUserDetailsService implements UserDetailsService {

    private final RegisterRepository registerRepository;
     @Autowired
    public CustomUserDetailsService(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Register user = registerRepository.findByUsername(username)
                .orElseThrow(() -> {
                    System.out.println("Пользователь не найден: " + username);
                    return new UsernameNotFoundException("Пользователь не найден: " + username);
                });

        // Пользователь найден, выводим его данные
        System.out.println("Попытка загрузить пользователя по имени пользователя: " + username);
        System.out.println("Найден пользователь: " + username + ", пароль: " + user.getPassword());

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }
}




