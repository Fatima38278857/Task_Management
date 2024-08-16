package com.example.program.Task_Management.dto;

import com.example.program.Task_Management.entity.UserEntity;
import com.example.program.Task_Management.enumm.Role;
import com.example.program.Task_Management.enumm.TaskPriority;
import com.example.program.Task_Management.enumm.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class TaskDTO {
    private Long id;
    @Size(min = 4, max = 32)
    @Schema(description = "заголовок объявления")
    private String title;
    @Size(min = 8, max = 64)
    @Schema(description = "описание объявления")
    private String description;
    @Schema(description = "Статус")
    private TaskStatus status;
    @Schema(description = "Приоритет")
    private TaskPriority priority;
    @Schema(description = "Роль")
    private Role role;
}
