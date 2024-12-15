package com.todolist.todolist.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Entity
@Table(name= "todo_items")
public class ToDoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "is_completed")
    private Boolean isCompleted;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "deadline", columnDefinition = "DATE")
    private LocalDate deadline;

    @Column(name = "user_id")
    private Long userId;

}
