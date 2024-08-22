package com.example.program.Task_Management.service.impl;

import com.example.program.Task_Management.claass.CommentRequest;
import com.example.program.Task_Management.claass.CreateOrUpdateTaskDTO;
import com.example.program.Task_Management.claass.TaskWithCommentsDTO;
import com.example.program.Task_Management.dto.CommentDTO;
import com.example.program.Task_Management.dto.TaskDTO;
import com.example.program.Task_Management.entity.CommentEntity;
import com.example.program.Task_Management.entity.TaskEntity;
import com.example.program.Task_Management.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface TaskService {

     void takeTask(Long taskId,   Long userId);

    CommentDTO addCommentToTask(Long taskId, CommentRequest commentRequest);

    List<TaskDTO> getTasksByUserId(Long userId);

    TaskDTO addTask(CreateOrUpdateTaskDTO properties, Long id);

    Optional<TaskEntity> getTask(Long id);
    void removeTask(Long id);

    TaskDTO updateTask(Long id, CreateOrUpdateTaskDTO properties, UserEntity currentUser);

}
