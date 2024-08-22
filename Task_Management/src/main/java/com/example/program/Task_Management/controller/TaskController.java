package com.example.program.Task_Management.controller;

import com.example.program.Task_Management.claass.*;
import com.example.program.Task_Management.dto.CommentDTO;
import com.example.program.Task_Management.entity.TaskEntity;
import com.example.program.Task_Management.entity.UserEntity;
import com.example.program.Task_Management.mapperr.TaskMapperr;
import com.example.program.Task_Management.dto.TaskDTO;
import com.example.program.Task_Management.mapper.TaskMapper;
import com.example.program.Task_Management.repository.TaskRepository;
import com.example.program.Task_Management.service.TaskImpl;
import com.example.program.Task_Management.service.UserImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.annotation.Secured;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private TaskImpl taskService;

    @Autowired
    private UserImpl userService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private TaskMapperr taskMapperr;


    @PostMapping("/assign-task")
    @Operation(summary = "Назначить задачу пользователю",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Задача успешно назначена"),
                    @ApiResponse(responseCode = "404", description = "Задача или пользователь не найдены"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
                    @ApiResponse(responseCode = "400", description = "Неверные данные запроса")
            })
    public ResponseEntity<String> assignTask(@RequestBody AssignTaskRequest request) {
        try {
            taskService.takeTask(request.getTaskId(), request.getUserId());
            return ResponseEntity.ok("Исполнитель задачи успешно назначен");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Получиить задачу и по Id user и все коментарии",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Задачи успешно получены"),
                    @ApiResponse(responseCode = "404", description = "User не найдет")
            })
    public List<TaskDTO> getTasksByUserId(@PathVariable Long userId) {
        return taskService.getTasksByUserId(userId);
    }

    @PostMapping("/{taskId}/comments")
    @Operation(summary = "Дабавить комментарии к задаче",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Комментарий успешно добавлен"),
                    @ApiResponse(responseCode = "404", description = "Такой задачи нет")
            })
    public ResponseEntity<CommentDTO> addCommentToTask(
            @PathVariable Long taskId,
            @RequestBody CommentRequest commentRequest) {
        System.out.println("Request UserEmail: " + commentRequest.getUserEmail());
        System.out.println("Request CommentText: " + commentRequest.getText());
        CommentDTO commentDTO = taskService.addCommentToTask(taskId, commentRequest);
        return new ResponseEntity<>(commentDTO, HttpStatus.CREATED);
    }


    @PostMapping("/add")
    @Operation(summary = "Create a new task",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Задача успешно создана"),
                    @ApiResponse(responseCode = "400", description = "Неверные данные запроса")
            })
    public ResponseEntity<TaskDTO> addTask(@RequestBody CreateOrUpdateTaskDTO properties, @RequestParam Long userId, Authentication authentication) {
        log.info("addAd in TaskController is used");
        TaskDTO createdTask = taskService.addTask(properties, userId);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);

    }

    @GetMapping("/{id}")
    @Operation(summary = "Получит задачу по id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Задача успешно получена"),
                    @ApiResponse(responseCode = "404", description = "Задача не найдена")
            })
    public ResponseEntity<TaskEntity> getTask(@PathVariable Long id) {
        Optional<TaskEntity> task = taskService.getTask(id);
        if (task.isPresent()) {
            return ResponseEntity.ok(task.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{id}/priority")
    @Operation(summary = "Обновить приоритет задачи",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Приоритет успешно обновлен"),
                    @ApiResponse(responseCode = "404", description = "Задача не найдена"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен")
            })
    public void updateTaskPriority(
            @PathVariable Long id,
            @RequestBody UpdateTaskPriorityDTO updateDto,
            @RequestHeader("User-Id") Long userId) throws Exception {

        UserEntity user = userService.findById(userId);
        taskService.updateTaskPriority(id, updateDto, user);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Обновить статус задачи",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Статус успешно обновлен"),
                    @ApiResponse(responseCode = "404", description = "Задача не найдена"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен")
            })
    public void updateTaskStatus(
            @PathVariable Long id,
            @RequestBody UpdateTaskStatusDTO updateDto,
            @RequestHeader("User-Id") Long userId) throws Exception {

        UserEntity user = userService.findById(userId);
        taskService.updateTaskStatus(id, updateDto, user);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить задачу",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Задача успешно обновленна"),
                    @ApiResponse(responseCode = "404", description = "Задача не найдена "),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен")
            })
    public ResponseEntity<TaskDTO> updateTask(
            @PathVariable Long id,
            @RequestBody CreateOrUpdateTaskDTO createOrUpdateTaskDTO,
            @AuthenticationPrincipal UserEntity currentUser) {
        TaskDTO updatedTask = taskService.updateTask(id, createOrUpdateTaskDTO, currentUser);
        return ResponseEntity.ok(updatedTask);
    }
    @Operation(
            summary = "Удаление задачи",
            description = "Удаляет задачу по указанному идентификатору. Требует аутентификации.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(name = "id", description = "Уникальный идентификатор задачи", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Задача успешно удалена"),
                    @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован"),
                    @ApiResponse(responseCode = "404", description = "Задача не найдена")
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> removeTask(@PathVariable Long id) {
        taskService.removeTask(id);
        return ResponseEntity.noContent().build();
    }
}
