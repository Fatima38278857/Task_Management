package com.example.program.Task_Management.repository;

import com.example.program.Task_Management.entity.CommentEntity;
import com.example.program.Task_Management.entity.TaskEntity;
import com.example.program.Task_Management.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    Optional<CommentEntity> findCommentById(Long id);
    List<TaskEntity> findByUserId(UserEntity userId);
    List<TaskEntity> findByUserId(Long userId);

}
