package com.todolist.todolist.controllers;


import com.todolist.todolist.models.ToDoItem;
import com.todolist.todolist.models.User;
import com.todolist.todolist.repositories.UserRepository;
import com.todolist.todolist.services.ToDoItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
public class HomeController {

    private ToDoItemService toDoItemService;
    private UserRepository userRepository;

    @Autowired
    public void setToDoItemService(ToDoItemService toDoItemService) {
        this.toDoItemService = toDoItemService;
    }

    @RequestMapping("/")
    public ModelAndView helloPage() {
        return new ModelAndView("startPage");
    }

    @RequestMapping("/home")
    public ModelAndView index(){
        System.out.println("Hi, i'm home endpoint");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByEmail(username).orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<ToDoItem> userToDoItems = toDoItemService.getToDoItemsByUserId(user.getId());
            ModelAndView modelAndView = new ModelAndView("home");
            modelAndView.addObject("toDoItems", userToDoItems);
        System.out.println("Home endpoint end his work");
            return modelAndView;
    }

    @GetMapping("/cannot-edit-this-todo-item")
    public ModelAndView cannotEditThisTodoItem() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByEmail(username).orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<ToDoItem> userToDoItems = toDoItemService.getToDoItemsByUserId(user.getId());
        ModelAndView modelAndView = new ModelAndView("cannot-edit-this-todo-item");
        modelAndView.addObject("toDoItems", userToDoItems);
        return modelAndView;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
