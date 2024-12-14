package com.todolist.todolist.models;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String email;
    @Column
    private String username;
    @Column
    private String password;

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<ToDoItem> toDoItems = null;
}
