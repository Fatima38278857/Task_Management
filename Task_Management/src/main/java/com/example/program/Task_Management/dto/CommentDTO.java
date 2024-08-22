package com.example.program.Task_Management.dto;

import com.example.program.Task_Management.entity.TaskEntity;
import com.example.program.Task_Management.entity.UserEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDTO {
    private Long id;
    private String text;
    private Long userId;
    private Long taskId;
    private LocalDateTime сreatedAt;


}
