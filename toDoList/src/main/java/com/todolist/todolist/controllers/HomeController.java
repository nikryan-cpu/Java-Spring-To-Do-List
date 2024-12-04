package com.todolist.todolist.controllers;


import com.todolist.todolist.models.ToDoItem;
import com.todolist.todolist.services.ToDoItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HomeController {

    private ToDoItemService toDoItemService;

    @Autowired
    public void setToDoItemService(ToDoItemService toDoItemService) {
        this.toDoItemService = toDoItemService;
    }

    @RequestMapping("/")
    public ModelAndView index(){
            ModelAndView modelAndView = new ModelAndView("index");
            modelAndView.addObject("toDoItems", toDoItemService.getToDoItems());
            return modelAndView;
    }
}
