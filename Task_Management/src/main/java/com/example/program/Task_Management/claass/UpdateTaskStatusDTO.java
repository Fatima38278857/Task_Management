package com.example.program.Task_Management.claass;

import com.example.program.Task_Management.enumm.TaskStatus;
import lombok.Data;

@Data
public class UpdateTaskStatusDTO {
    private TaskStatus status;
}
