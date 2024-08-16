package com.example.program.Task_Management.dto;

import lombok.Data;

@Data
public class CommentDTO {
    private Long id;
    private String text;
    private Long taskId;
    private Long userId;
}
