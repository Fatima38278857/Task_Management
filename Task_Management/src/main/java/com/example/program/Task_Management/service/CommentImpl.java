package com.example.program.Task_Management.service;

import com.example.program.Task_Management.claass.CreateOrUpdateComment;
import com.example.program.Task_Management.dto.CommentDTO;
import com.example.program.Task_Management.entity.CommentEntity;
import com.example.program.Task_Management.mapper.CommentMapper;
import com.example.program.Task_Management.repository.CommentRepository;
import com.example.program.Task_Management.service.impl.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentImpl implements CommentService {

    private CommentRepository commentRepository;
    private CommentMapper commentMapper;
    private CommentDTO commentDTO;

    @Override
    public List<CommentDTO> getAllComment(long taskId) {
        List<CommentEntity> comment = commentRepository.findAll();
        return comment.stream().map(commentMapper::toDTO)
                .collect(Collectors.toList());

    }

    @Override
    public CommentDTO createComment(long task_Id, CommentEntity comment) {
        return null;
    }

    @Override
    public void deleteComment(long task_Id, long comment_Id) {

    }

    @Override
    public CommentDTO patchCommentId(long task_Id, long comment_Id, CreateOrUpdateComment createOrUpdateComment) {
        return null;
    }
}
