package com.example.Task_Management.dto;




import com.example.Task_Management.enums.TaskPriority;
import lombok.Data;

@Data
public class UpdateTaskPriorityDTO {
    private TaskPriority priority;
}
