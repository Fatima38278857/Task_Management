package com.example.Task_Management.dto;




import com.example.Task_Management.enums.TaskPriority;
import com.example.Task_Management.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(name = "CreateOrUpdateTask")
public class CreateOrUpdateTaskDTO {
    @Schema(description = "заголовок задачи")
    private String title;
    @Schema(description = "описание задачи")
    private String description;
    @Schema(description = "статус задачи")
    private TaskStatus status;
    @Schema(description = "приоритет задачи")
    private TaskPriority taskPriority;


}
