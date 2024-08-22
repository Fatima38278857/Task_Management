package com.example.program.Task_Management.claass;

import lombok.Data;

@Data
public class AssignTaskRequest {
    private Long taskId;
    private Long userId;
}
