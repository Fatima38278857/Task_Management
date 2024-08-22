package com.example.program.Task_Management.service;

import com.example.program.Task_Management.claass.CommentRequest;
import com.example.program.Task_Management.claass.CreateOrUpdateComment;
import com.example.program.Task_Management.dto.CommentDTO;
import com.example.program.Task_Management.dto.UserDTO;
import com.example.program.Task_Management.entity.CommentEntity;
import com.example.program.Task_Management.entity.TaskEntity;
import com.example.program.Task_Management.entity.UserEntity;
import com.example.program.Task_Management.exception.NotFoundCommentException;
import com.example.program.Task_Management.mapperr.CommentMapperr;
import com.example.program.Task_Management.mapperr.UserMapperr;
import com.example.program.Task_Management.repository.CommentRepository;
import com.example.program.Task_Management.repository.TaskRepository;
import com.example.program.Task_Management.repository.UserRepository;
import com.example.program.Task_Management.service.impl.CommentService;
import com.example.program.Task_Management.service.impl.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*

 */
@Service
public class CommentImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final UserService userService;
    private final UserMapperr userMapperr;
    private final CommentMapperr commentMapperr;

    @Autowired
    public CommentImpl(CommentRepository commentRepository, UserRepository userRepository, TaskRepository taskRepository, UserService userService, UserMapperr userMapperr, CommentMapperr commentMapperr) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.userMapperr = userMapperr;
        this.commentMapperr = commentMapperr;
    }
    @Override
    public CommentEntity saveComment(CommentEntity comment) {
        return commentRepository.save(comment);
    }

    /*

     */
    @Override
    public List<CommentDTO> getAllComment(long taskId) {
        List<CommentEntity> comment = commentRepository.findAll();
        return comment.stream().map(commentMapperr::toDTO)
                .collect(Collectors.toList());

    }

    /*

     */
//    @Override
//    public CommentDTO createComment(Long taskId, String userEmail, CommentEntity comment) {
//        if (userEmail == null || userEmail.isEmpty()) {
//            throw new IllegalArgumentException("Email пользователя не может быть null или пустым");
//        }
//
//        // Получаем текущего пользователя по email
//        UserDTO currentUser = userService.getUserByEmail(userEmail);
//        if (currentUser == null) {
//            throw new IllegalStateException("Пользователь не найден");
//        }
//        // Ищем задачу по ID
//        TaskEntity task = taskRepository.findById(taskId)
//                .orElseThrow(() -> new NotFoundCommentException("Задача не найдена"));
//        // Проверяем текст комментария
//        String text = comment.getText();
//        if (text == null || text.trim().isEmpty()) {
//            throw new IllegalArgumentException("Текст комментария не может быть пустым");
//        }
//        // Преобразуем UserDTO в UserEntity
//        UserEntity userEntity = userMapperr.toEntity(currentUser);
//        // Устанавливаем значения для комментария
//        comment.setTask(task);
//        comment.setUserId(userEntity);
//        comment.setСreatedAt(LocalDateTime.now());
//        // Сохраняем комментарий и возвращаем его DTO
//        CommentEntity savedComment = commentRepository.save(comment);
//        return commentMapperr.toDTO(savedComment);
//    }


    @Override
    public void deleteComment(long task_Id, long comments_Id) {
        commentRepository.deleteCommentByTaskIdAndId(task_Id, comments_Id);
    }

    /*

     */
    @Override
    public CommentDTO updateCommentId(long taskId, long commentsId, CreateOrUpdateComment createOrUpdateComment) {
        UserDTO userDTO = userService.getUser();
        Long authorId = userDTO.getId();
        // Поиск комментария с использованием Optional
        Optional<CommentEntity> optionalComment = commentRepository.findComments(taskId, authorId, commentsId).stream().findFirst();
        // Если комментарий не найден
        if (optionalComment.isEmpty()) {
            throw new NotFoundCommentException("Комментарий не найден или вы не имеете к нему доступа.");
        }
        CommentEntity comment = optionalComment.get();
        // Обновление текста комментария
        comment.setText(createOrUpdateComment.getText());
        // Сохранение комментария
        commentRepository.save(comment);
        // Возврат обновленного комментария в виде DTO
        return commentMapperr.toDTO(comment);
    }



}
