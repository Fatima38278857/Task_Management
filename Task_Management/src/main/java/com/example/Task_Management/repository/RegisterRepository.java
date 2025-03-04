package com.example.Task_Management.repository;

import com.example.Task_Management.entity.Register;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegisterRepository extends JpaRepository<Register, Long> {

    Optional<Register> findByUsername(String username);
}
