package com.example.Task_Management.service;


import com.example.Task_Management.claass.CreateOrUpdateComment;
import com.example.Task_Management.dto.CommentDTO;
import com.example.Task_Management.dto.UserDTO;
import com.example.Task_Management.entity.CommentEntity;
import com.example.Task_Management.exception.NotFoundCommentException;
import com.example.Task_Management.impl.CommentService;
import com.example.Task_Management.impl.UserService;
import com.example.Task_Management.mapperImpl.CommentMapperImpl;
import com.example.Task_Management.mapperImpl.UserMapperImpl;
import com.example.Task_Management.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сервис для управления комментариями.
 * Этот класс предоставляет методы для выполнения CRUD-операций с комментариями,
 * включая добавление, удаление, обновление и получение комментариев. Он взаимодействует
 * с репозиториями и мапперами для работы с базой данных и преобразования данных.</p>
 * Основные функции:
 * - Сохранение комментариев в базе данных.
 * - Получение всех комментариев, связанных с задачей.
 * - Удаление комментариев по идентификатору.
 * - Обновление текста комментария с проверкой прав доступа.
 * Используемые зависимости:
 * - {@link CommentRepository} — для работы с комментариями.
 * - {@link UserService} — для получения текущего пользователя.
 * - {@link UserMapperImpl} — для преобразования пользователей в DTO.
 * - {@link CommentMapperImpl} — для преобразования комментариев в DTO.
 */
@Service
public class CommentImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final CommentMapperImpl commentMapperImpl;

    @Autowired
    public CommentImpl(CommentRepository commentRepository, UserService userService, CommentMapperImpl commentMapperr) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.commentMapperImpl = commentMapperr;
    }

    /**
     * Сохраняет комментарий в репозитории.
     *
     * @param comment Сущность комментария для сохранения.
     * @return Сохраненная сущность комментария.
     */
    @Override
    public CommentEntity saveComment(CommentEntity comment) {
        return commentRepository.save(comment);
    }

    /**
     * Получает список всех комментариев, связанных с задачей.
     *
     * @param taskId Идентификатор задачи.
     * @return Список комментариев в формате DTO.
     */
    @Override
    public List<CommentDTO> getAllComment(long taskId) {
        List<CommentEntity> comment = commentRepository.findAll();
        return comment.stream().map(commentMapperImpl::toDTO)
                .collect(Collectors.toList());

    }

    /**
     * Удаляет комментарий по идентификатору задачи и идентификатору комментария.
     *
     * @param task_Id     Идентификатор задачи.
     * @param comments_Id Идентификатор комментария.
     */
    @Override
    public void deleteComment(long task_Id, long comments_Id) {
        commentRepository.deleteCommentByTaskIdAndId(task_Id, comments_Id);
    }

    /**
     * Обновляет текст комментария по идентификатору задачи, идентификатору комментария и данным для обновления.
     *
     * @param taskId                Идентификатор задачи.
     * @param commentsId            Идентификатор комментария.
     * @param createOrUpdateComment DTO с новыми данными для обновления комментария.
     * @return DTO обновленного комментария.
     * @throws NotFoundCommentException Если комментарий не найден или текущий пользователь не имеет к нему доступа.
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
        return commentMapperImpl.toDTO(comment);
    }

}
