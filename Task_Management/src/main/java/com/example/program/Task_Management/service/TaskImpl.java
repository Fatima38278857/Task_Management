package com.example.program.Task_Management.service;

import com.example.program.Task_Management.claass.CreateOrUpdateTaskDTO;
import com.example.program.Task_Management.dto.TaskDTO;
import com.example.program.Task_Management.dto.UserDTO;
import com.example.program.Task_Management.entity.CommentEntity;
import com.example.program.Task_Management.entity.TaskEntity;
import com.example.program.Task_Management.entity.UserEntity;
import com.example.program.Task_Management.enumm.Role;
import com.example.program.Task_Management.enumm.TaskStatus;
import com.example.program.Task_Management.exception.NoRights;
import com.example.program.Task_Management.exception.TaskNotFoundException;
import com.example.program.Task_Management.mapper.TaskMapper;
import com.example.program.Task_Management.mapper.UserMapper;
import com.example.program.Task_Management.repository.TaskRepository;
import com.example.program.Task_Management.repository.UserRepository;
import com.example.program.Task_Management.service.impl.TaskService;
import com.example.program.Task_Management.service.impl.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TaskImpl implements TaskService {
    private static final Logger log = LoggerFactory.getLogger(TaskImpl.class);
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public TaskImpl(TaskRepository taskRepository, TaskMapper taskMapper, UserService userService, UserRepository userRepository, UserMapper userMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.userService = userService;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public List<TaskDTO> getAllTask() {
        // Получаем все задачи из репозитория, преобразуем их в TaskDTO и возвращаем список
        return taskRepository.findAll()
                .stream().filter(taskEntity -> taskEntity.getStatus() == TaskStatus.PENDING)
                .map(taskMapper::taskToTaskDTO)
                .toList();
    }


    @Override
    public TaskDTO addTask(TaskEntity task, UserEntity userEntity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("Информация об аутентификации не найдена");
        }
        // Получаем роль аутентифицированного пользователя
        boolean isAuthor = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(Role.AUTHOR.name()));

        // Проверяем, что пользователь имеет роль AUTHOR
        if (!isAuthor) {
            throw new IllegalStateException("Только пользователи с ролью 'AUTHOR' могут создавать задачи");
        }
        // Получаем email текущего пользователя
        String email;
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }
        // Получаем пользователя по email
        UserEntity user = userService.getUser(email);
        task.setUser(user);
        // Сохраняем задачу
        TaskEntity savedTask = taskRepository.save(task);
        return taskMapper.taskToTaskDTO(savedTask);
    }

    public TaskDTO getTask(Long id) {
        return taskMapper.taskToTaskDTO(taskRepository.findById(id).orElseThrow(TaskNotFoundException::new));
    }

    @Override
    public void removeTask(Long id) {
        taskRepository.deleteById(id);

    }

    public UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserEntity) authentication.getPrincipal();
    }

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


    public TaskDTO takeTask(long id) {
        UserDTO user = userService.getUser();
        TaskEntity taskEntity = taskRepository.findById(id).orElseThrow();
        taskEntity.setUserExecutor(userMapper.toEntity(user));
        TaskEntity save = taskRepository.save(taskEntity);

        return taskMapper.taskToTaskDTO(save);

    }

    @Override
    public CommentEntity getCommentByID(Long id) {
        return taskRepository.findCommentById(id).get();
    }

}
