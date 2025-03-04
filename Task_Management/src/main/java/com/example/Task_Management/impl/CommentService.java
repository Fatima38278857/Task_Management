package com.example.Task_Management.impl;





import com.example.Task_Management.claass.CreateOrUpdateComment;
import com.example.Task_Management.dto.CommentDTO;
import com.example.Task_Management.entity.CommentEntity;

import java.util.List;

public interface CommentService {
    List<CommentDTO> getAllComment(long taskId);


    void deleteComment(long task_Id, long comment_Id);
    CommentEntity saveComment(CommentEntity comment);
    CommentDTO updateCommentId(long task_Id, long comment_Id, CreateOrUpdateComment createOrUpdateComment);

}