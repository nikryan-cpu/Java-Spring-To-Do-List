package com.todolist.todolist.services;

import com.todolist.todolist.models.ToDoItem;
import com.todolist.todolist.repositories.ToDoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class ToDoItemService {

    private ToDoItemRepository toDoItemRepository;

    @Autowired
    public void setToDoItemService(ToDoItemRepository toDoItemRepository) {
        this.toDoItemRepository = toDoItemRepository;
    }

    public Iterable<ToDoItem> getToDoItems() {
        return toDoItemRepository.findAll();
    }

    public Optional<ToDoItem> getToDoItemById(Long id) {
        return toDoItemRepository.findById(id);
    }

    public List<ToDoItem> getToDoItemsByUserId(Long userId) {
        return toDoItemRepository.findByUserId(userId);
    }

    public ToDoItem save(ToDoItem toDoItem) {
        if(toDoItem.getId() == null) {
            toDoItem.setCreatedAt(Instant.now());
        }
        return toDoItemRepository.save(toDoItem);
    }

    public void delete(ToDoItem toDoItem) {
            toDoItemRepository.delete(toDoItem);
    }

}
