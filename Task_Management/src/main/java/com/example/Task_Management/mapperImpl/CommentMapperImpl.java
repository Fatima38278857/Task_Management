package com.example.Task_Management.mapperImpl;



import com.example.Task_Management.dto.CommentDTO;
import com.example.Task_Management.entity.CommentEntity;
import com.example.Task_Management.entity.TaskEntity;
import com.example.Task_Management.entity.UserEntity;
import com.example.Task_Management.repository.TaskRepository;
import com.example.Task_Management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentMapperImpl {
    @Autowired
    private TaskRepository taskRepository;
    private UserRepository userRepository;

    public CommentDTO toDTO(CommentEntity comment) {
        if (comment == null) {
            return null;
        }

        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setUserId(comment.getUserId().getId());
        dto.setTaskId(comment.getTask().getId());
        dto.setСreatedAt(comment.getСreatedAt());
        if (comment.getTask() != null) {
            dto.setTaskId(comment.getTask().getId());
        }
        return dto;
    }

    public CommentEntity toEntity(CommentDTO dto) {
        if (dto == null) {
            return null;
        }
        CommentEntity comment = new CommentEntity();
        comment.setId(dto.getId());
        comment.setText(dto.getText());
        UserEntity user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        comment.setUserId(user);

        // Поиск TaskEntity по идентификатору и установка
        TaskEntity task = taskRepository.findById(dto.getTaskId()).orElseThrow(() -> new RuntimeException("Task not found"));
        comment.setTask(task);
        comment.setСreatedAt(dto.getСreatedAt());
        return comment;
    }
}
