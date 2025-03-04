package com.example.Task_Management.claass;

import lombok.Data;

@Data
public class AssignTaskRequest {
    private Long taskId;
    private Long userId;
}
