package com.example.program.Task_Management.repository;

import com.example.program.Task_Management.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
}
