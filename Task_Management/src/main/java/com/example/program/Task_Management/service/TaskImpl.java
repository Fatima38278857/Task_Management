package com.example.program.Task_Management.service;

import com.example.program.Task_Management.claass.*;
import com.example.program.Task_Management.dto.CommentDTO;
import com.example.program.Task_Management.exception.ResourceNotFoundException;
import com.example.program.Task_Management.mapperr.CommentMapperr;
import com.example.program.Task_Management.mapperr.TaskMapperr;
import com.example.program.Task_Management.dto.UserDTO;
import com.example.program.Task_Management.enumm.Role;
import com.example.program.Task_Management.exception.NoRights;
import com.example.program.Task_Management.exception.TaskNotFoundException;
import com.example.program.Task_Management.repository.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;

import org.springframework.transaction.annotation.Transactional;
import com.example.program.Task_Management.dto.TaskDTO;
import com.example.program.Task_Management.entity.CommentEntity;
import com.example.program.Task_Management.entity.TaskEntity;
import com.example.program.Task_Management.entity.UserEntity;
import com.example.program.Task_Management.enumm.TaskStatus;
import com.example.program.Task_Management.mapper.TaskMapper;
import com.example.program.Task_Management.mapper.UserMapper;
import com.example.program.Task_Management.repository.TaskRepository;
import com.example.program.Task_Management.repository.UserRepository;
import com.example.program.Task_Management.service.impl.TaskService;
import com.example.program.Task_Management.service.impl.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*

 */
@Service
public class TaskImpl implements TaskService {
    private static final Logger log = LoggerFactory.getLogger(TaskImpl.class);
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TaskMapperr taskMapperr;
    private final CommentRepository commentRepository;
    private final CommentMapperr commentMapperr;


    @Autowired
    public TaskImpl(TaskRepository taskRepository, TaskMapper taskMapper, UserService userService, UserRepository userRepository, UserMapper userMapper, TaskMapperr taskMapperr, CommentRepository commentRepository, CommentMapperr commentMapperr) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.userService = userService;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.taskMapperr = taskMapperr;
        this.commentRepository = commentRepository;
        this.commentMapperr = commentMapperr;
    }

    @Override
    public List<TaskDTO> getTasksByUserId(Long userId) {
        List<TaskEntity> tasks = taskRepository.findByUserId(userId);
        return tasks.stream().map(taskMapperr::taskDTO).collect(Collectors.toList());
    }

    /*

     */

    @Override
    public TaskDTO addTask(CreateOrUpdateTaskDTO properties, Long id) {
        UserEntity user = userService.findById(id);
        // Проверка, что у пользователя роль AUTHOR
        if (user.getRole() != Role.AUTHOR) {
            throw new AccessDeniedException("У вас нет роли АВТОРА для создания задачи");
        }
        TaskEntity taskEntity = taskMapperr.createOrUpdateAdToAd(properties, user);
        TaskEntity savedTaskEntity = taskRepository.save(taskEntity);
        return taskMapperr.taskDTO(savedTaskEntity);

    }

    /*

     */
    @Override
    public void takeTask(Long taskId, Long userId) {
        // Получение текущего аутентифицированного пользователя
        UserEntity user = userService.getAuthenticatedUser();
        // Проверка роли пользователя
        if (!user.getRole().equals(Role.AUTHOR)) {
            throw new AccessDeniedException("Только пользователи с ролью 'AUTHOR' могут назначать задачи");
        }
        // Поиск задачи по ID
        TaskEntity taskEntity = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Задача не найдена"));
        // Проверка, что текущий пользователь создал эту задачу
        if (!taskEntity.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Вы можете назначать исполнителей только для своих задач");
        }
        // Поиск пользователя-исполнителя
        UserEntity executorEntity = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
        // Проверка роли пользователя-исполнителя
        if (!executorEntity.getRole().equals(Role.EXECUTOR)) {
            throw new IllegalArgumentException("Пользователю нужно иметь роль 'EXECUTOR', чтобы быть назначенным исполнителем задачи");
        }
        // Назначение пользователя-исполнителя для задачи
        taskEntity.setUserExecutor(executorEntity);
        // Обновление поля currentTask у пользователя-исполнителя
        executorEntity.setCurrentTask(taskEntity);
        // Сохранение изменений в репозитории
        taskRepository.save(taskEntity);
        userRepository.save(executorEntity);
    }


    @Override
    public Optional<TaskEntity> getTask(Long id) {
        return Optional.ofNullable(taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found")));
    }
        @Override
        @Transactional
        public void removeTask(Long id) {
        // Получение текущего аутентифицированного пользователя
        UserEntity currentUser = userService.getAuthenticatedUser();
        if (currentUser == null) {
            throw new AccessDeniedException("Пользователь не аутентифицирован");
        }

        TaskEntity task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Задача с ID " + id + " не найдена"));

        // Проверка, что пользователь может удалить только свою задачу
        if (!task.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Вы не имеете прав на удаление этой задачи");
        }

        taskRepository.delete(task);
    }

    /*

     */
    @Override
    public TaskDTO updateTask(Long id, CreateOrUpdateTaskDTO createOrUpdateTaskDTO, UserEntity currentUser) {
        TaskEntity task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id " + id));
        // Осуществляю обновление поля на основе роли текущего пользователя
        if (currentUser.getRole() == Role.AUTHOR) {
            if (createOrUpdateTaskDTO.getTitle() != null) {
                task.setTitle(createOrUpdateTaskDTO.getTitle());
            }
            if (createOrUpdateTaskDTO.getDescription() != null) {
                task.setDescription(createOrUpdateTaskDTO.getDescription());
            }
            if (createOrUpdateTaskDTO.getStatus() != null) {
                task.setStatus(createOrUpdateTaskDTO.getStatus());
            }
        } else {
            throw new NoRights("У пользователя нет прав для обновления этой задачи");
        }
        TaskEntity updatedTask = taskRepository.save(task);
        return taskMapper.taskToTaskDTO(updatedTask);
    }

    @Override
    public CommentDTO addCommentToTask(Long taskId, CommentRequest commentRequest) {
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));

        UserEntity user = userRepository.findByEmail(commentRequest.getUserEmail());
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + commentRequest.getUserEmail());
        }

        CommentEntity comment = new CommentEntity();
        comment.setTask(task);
        comment.setUserId(user);
        comment.setText(commentRequest.getText());
        System.out.println("Comment text: " + commentRequest.getText());
        comment.setСreatedAt(LocalDateTime.now());

        CommentEntity savedComment = commentRepository.save(comment);

        // Преобразуем сохраненный комментарий в DTO

        return commentMapperr.toDTO(savedComment);
    }

    @Transactional
    public void updateTaskPriority(Long taskId, UpdateTaskPriorityDTO updateDto, UserEntity user) throws Exception {
        if (user.getRole() != Role.AUTHOR) {
            throw new AccessDeniedException("У пользователя нет разрешения на обновление приоритета задачи");
        }
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(() -> new Exception("Task not found"));
        task.setPriority(updateDto.getPriority());
        taskRepository.save(task);
    }

    @Transactional
    public void updateTaskStatus(Long taskId, UpdateTaskStatusDTO updateDto, UserEntity user) throws Exception {
        if (user.getRole() != Role.EXECUTOR) {
            throw new AccessDeniedException("У пользователя нет разрешения на обновление статуса задачи");
        }
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(() -> new Exception("Task not found"));
        task.setStatus(updateDto.getStatus());
        taskRepository.save(task);
    }
}
