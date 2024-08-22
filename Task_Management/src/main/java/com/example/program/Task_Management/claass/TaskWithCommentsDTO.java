package com.example.program.Task_Management.claass;

import com.example.program.Task_Management.dto.CommentDTO;
import com.example.program.Task_Management.entity.UserEntity;
import lombok.Data;

import java.util.List;
@Data
public class TaskWithCommentsDTO {

    private Long id;
    private String title;
    private String description;
    private UserEntity userId;
    private List<CommentDTO> comments;
}
