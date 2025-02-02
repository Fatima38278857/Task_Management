package com.example.program.Task_Management.repository;

import com.example.program.Task_Management.entity.Register;
import com.example.program.Task_Management.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegisterRepository extends JpaRepository<Register, Long> {

    Optional<Register> findByUsername(String username);
}
