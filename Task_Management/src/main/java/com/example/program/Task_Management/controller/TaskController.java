package com.example.program.Task_Management.controller;

import com.example.program.Task_Management.claass.CreateOrUpdateTaskDTO;
import com.example.program.Task_Management.dto.TaskDTO;
import com.example.program.Task_Management.dto.UserDTO;
import com.example.program.Task_Management.entity.TaskEntity;
import com.example.program.Task_Management.entity.UserEntity;
import com.example.program.Task_Management.mapper.TaskMapper;
import com.example.program.Task_Management.repository.TaskRepository;
import com.example.program.Task_Management.service.TaskImpl;
import com.example.program.Task_Management.service.UserImpl;
import com.example.program.Task_Management.service.impl.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;


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


    @Operation(tags = "Задача", summary = "Добавление Задачи", responses = {@ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TaskDTO.class))), @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content())})
    @PreAuthorize("hasAnyRole('AUTHOR')")
    @PostMapping("/add")
    public ResponseEntity<TaskDTO> addTask(@RequestBody TaskEntity task, UserEntity userEntity) {
        try {
            TaskDTO taskDTO = taskService.addTask(task, userEntity);
            return ResponseEntity.ok(taskDTO);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }

    }
    @Operation(tags = "Задача", summary = "Изменение Id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TaskDTO.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content()),
                    @ApiResponse(responseCode = "403", content = @Content)})
    @PutMapping("/{task_id}/take")
    public ResponseEntity<TaskDTO> takeTask(@PathVariable ("task_id") long id, Authentication authentication){
        log.info("В takeTask ");
        if (authentication.isAuthenticated()) {
            if (taskService.getTask(id) == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } else {
                return ResponseEntity.ok(taskService.takeTask(id));
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(tags = "Задача", summary = "Изменение Задачи",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TaskDTO.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content()),
                    @ApiResponse(responseCode = "403", content = @Content)})
    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable ("id") long id, CreateOrUpdateTaskDTO taskDTO, UserEntity currentUser, Authentication authentication) {
        log.info("В обновлении задачи");
        if (authentication.isAuthenticated()) {
            if (taskService.getTask(id) == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } else {
                return ResponseEntity.ok(taskService.updateTask(id, taskDTO, currentUser));
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
