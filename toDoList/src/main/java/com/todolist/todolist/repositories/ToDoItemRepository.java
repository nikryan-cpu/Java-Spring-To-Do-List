package com.todolist.todolist.repositories;

import com.todolist.todolist.models.ToDoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToDoItemRepository extends JpaRepository<ToDoItem, Long> {
    List<ToDoItem> findByUserId(Long userId);
}
