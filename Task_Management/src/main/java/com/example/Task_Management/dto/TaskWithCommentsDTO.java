package com.example.Task_Management.dto;



import com.example.Task_Management.entity.UserEntity;
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
