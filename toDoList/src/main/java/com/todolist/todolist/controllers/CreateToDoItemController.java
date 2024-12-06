package com.todolist.todolist.controllers;


import com.todolist.todolist.models.ToDoItem;
import com.todolist.todolist.services.ToDoItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class CreateToDoItemController {

    private ToDoItemService toDoItemService;

    @Autowired
    public void setToDoItemService(ToDoItemService toDoItemService) {
        this.toDoItemService = toDoItemService;
    }

    @GetMapping("/create-todo")
    public ModelAndView showCreateForm(ToDoItem toDoItem){
        ModelAndView modelAndView = new ModelAndView("new-todo-item");
        modelAndView.addObject("toDoItem", new ToDoItem());
        return modelAndView;
    }

    @PostMapping("/todo")
    public ResponseEntity<?> createToDoItem(@Validated ToDoItem toDoItem, BindingResult bindingResult, Model model){
        ToDoItem item = new ToDoItem();
        item.setDescription(toDoItem.getDescription());
        item.setIsCompleted(toDoItem.getIsCompleted());

        toDoItemService.save(item);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/"); // Укажите целевой URL
        return new ResponseEntity<>(headers, HttpStatus.FOUND); // 302 статус
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteTodoItem(@PathVariable("id") Long id, Model model) {
        ToDoItem todoItem = toDoItemService
                .getToDoItemById(id)
                .orElseThrow(() -> new IllegalArgumentException("TodoItem id: " + id + " not found"));

        toDoItemService.delete(todoItem);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/"); // Укажите целевой URL
        return new ResponseEntity<>(headers, HttpStatus.FOUND); // 302 статус
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") Long id, Model model) {
        ToDoItem toDoItem = toDoItemService
                .getToDoItemById(id)
                .orElseThrow(() -> new IllegalArgumentException("TodoItem id: " + id + " not found"));
        ModelAndView modelAndView = new ModelAndView("edit-todo-item");
        modelAndView.addObject("toDoItem", toDoItem);
        return modelAndView;
    }

    @PostMapping("/todo/{id}")
    public ResponseEntity<?> updateTodoItem(@PathVariable("id") Long id, @Validated ToDoItem todoItem, BindingResult result, Model model) {

        ToDoItem toDoItem = toDoItemService
                .getToDoItemById(id)
                .orElseThrow(() -> new IllegalArgumentException("TodoItem id: " + id + " not found"));

        toDoItem.setIsCompleted(todoItem.getIsCompleted());
        toDoItem.setDescription(todoItem.getDescription());

        toDoItemService.save(toDoItem);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/"); // Укажите целевой URL
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
