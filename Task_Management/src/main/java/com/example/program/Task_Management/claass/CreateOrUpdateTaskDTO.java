package com.example.program.Task_Management.claass;

import com.example.program.Task_Management.enumm.TaskPriority;
import com.example.program.Task_Management.enumm.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(name = "CreateOrUpdateTask")
public class CreateOrUpdateTaskDTO {
    @Schema(description = "заголовок объявления")
    private String title;
    @Schema(description = "описание объявления")
    private String description;
    @Schema(description = "статус объявления")
    private TaskStatus status;
}
