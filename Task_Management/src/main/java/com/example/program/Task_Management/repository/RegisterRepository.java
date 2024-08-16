package com.example.program.Task_Management.repository;

import com.example.program.Task_Management.entity.Register;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisterRepository extends JpaRepository<Register, Long> {
}
