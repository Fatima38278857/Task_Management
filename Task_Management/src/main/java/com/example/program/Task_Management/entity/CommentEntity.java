package com.example.program.Task_Management.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Setter
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
public class CommentEntity {
    @Column(name = "comments_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    @JsonIgnore
    private TaskEntity task;
    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    private UserEntity userId;

    private LocalDateTime сreatedAt;

}
