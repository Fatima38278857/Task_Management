package com.example.program.Task_Management.service.impl;

import com.example.program.Task_Management.claass.CreateOrUpdateTaskDTO;
import com.example.program.Task_Management.dto.TaskDTO;
import com.example.program.Task_Management.entity.CommentEntity;
import com.example.program.Task_Management.entity.TaskEntity;
import com.example.program.Task_Management.entity.UserEntity;

import java.util.List;

public interface TaskService {
    List<TaskDTO> getAllTask();

    // Добавление задачи
    TaskDTO addTask(TaskEntity task,  UserEntity userEntity);

    // Получение задачи по id
    TaskDTO getTask(Long id);

    // Удоление задачи
    void removeTask(Long id);

    TaskDTO updateTask(Long id, CreateOrUpdateTaskDTO properties, UserEntity currentUser);

    CommentEntity getCommentByID(Long id);
}
