package com.todolist.todolist.controllers;

import com.todolist.todolist.models.ToDoItem;
import com.todolist.todolist.repositories.UserRepository;
import com.todolist.todolist.services.ToDoItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


import java.util.Objects;

@RestController
public class CreateToDoItemController {

    UserRepository userRepository;


    private UserDetailsService userDetailsService;

    private ToDoItemService toDoItemService;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setToDoItemService(ToDoItemService toDoItemService) {
        this.toDoItemService = toDoItemService;
    }


    @Autowired
    public void userDetailsService(UserDetailsService userDetailsService){
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/create-todo")
    public ModelAndView showCreateForm(){
        ModelAndView modelAndView = new ModelAndView("new-todo-item");
        modelAndView.addObject("toDoItem", new ToDoItem());
        return modelAndView;
    }

    @PostMapping("/todo")
    public ResponseEntity<?> createToDoItem(@Validated ToDoItem toDoItem){
        HttpHeaders headers = new HttpHeaders();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        ToDoItem item = new ToDoItem();
        item.setDescription(toDoItem.getDescription());
        item.setIsCompleted(toDoItem.getIsCompleted());
        item.setDeadline(toDoItem.getDeadline());
        item.setUserId(userRepository.findByEmail(username).orElseThrow().getId());

        toDoItemService.save(item);
        headers.add("Location", "/home"); // Укажите целевой URL
        return new ResponseEntity<>(headers, HttpStatus.FOUND); // 302 статус
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteTodoItem(@PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if(!Objects.equals(userRepository.findByEmail(username).orElseThrow().getId(), toDoItemService.getToDoItemById(id).orElseThrow().getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can't delete this item");
        }
        ToDoItem todoItem = toDoItemService
                .getToDoItemById(id)
                .orElseThrow(() -> new IllegalArgumentException("TodoItem id: " + id + " not found"));

        toDoItemService.delete(todoItem);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/home"); // Укажите целевой URL
        return new ResponseEntity<>(headers, HttpStatus.FOUND); // 302 статус
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if(!Objects.equals(userRepository.findByEmail(username).orElseThrow().getId(), toDoItemService.getToDoItemById(id).orElseThrow().getUserId())) {
            return new ModelAndView("redirect:/cannot-edit-this-todo-item");
        }
        ToDoItem toDoItem = toDoItemService
                .getToDoItemById(id)
                .orElseThrow(() -> new IllegalArgumentException("TodoItem id: " + id + " not found"));
        ModelAndView modelAndView = new ModelAndView("edit-todo-item");
        modelAndView.addObject("toDoItem", toDoItem);
        return modelAndView;
    }


    @PostMapping("/todo/{id}")
    public ResponseEntity<?> updateTodoItem(@PathVariable("id") Long id, @Validated ToDoItem todoItemTmp) {
        ToDoItem toDoItem = toDoItemService
                .getToDoItemById(id)
                .orElseThrow(() -> new IllegalArgumentException("TodoItem id: " + id + " not found"));

        toDoItem.setIsCompleted(todoItemTmp.getIsCompleted());
        toDoItem.setDescription(todoItemTmp.getDescription());
        toDoItem.setDeadline(todoItemTmp.getDeadline());
        toDoItemService.save(toDoItem);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/home"); // Укажите целевой URL
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
