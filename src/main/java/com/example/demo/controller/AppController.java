package com.example.demo.controller;


import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;



@Controller
public class AppController {

    @Autowired
    private UserRepository repository;


    public AppController() {
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

}








