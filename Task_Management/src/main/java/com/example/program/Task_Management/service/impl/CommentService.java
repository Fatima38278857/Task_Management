package com.example.program.Task_Management.service.impl;

import com.example.program.Task_Management.claass.CreateOrUpdateComment;
import com.example.program.Task_Management.dto.CommentDTO;
import com.example.program.Task_Management.entity.CommentEntity;

import java.util.List;

public interface CommentService {
    List<CommentDTO> getAllComment(long taskId);


    void deleteComment(long task_Id, long comment_Id);
    CommentEntity saveComment(CommentEntity comment);
    CommentDTO updateCommentId(long task_Id, long comment_Id, CreateOrUpdateComment createOrUpdateComment);

}