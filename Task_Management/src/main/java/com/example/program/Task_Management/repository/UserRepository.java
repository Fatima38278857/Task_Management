package com.example.program.Task_Management.repository;

import com.example.program.Task_Management.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

     UserEntity findByEmailIgnoreCase(String email);

    Optional<UserEntity> findByUsername(String username);
}


