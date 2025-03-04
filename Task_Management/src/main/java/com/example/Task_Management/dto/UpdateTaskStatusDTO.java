package com.example.Task_Management.dto;




import com.example.Task_Management.enums.TaskStatus;
import lombok.Data;

@Data
public class UpdateTaskStatusDTO {
    private TaskStatus status;
}
