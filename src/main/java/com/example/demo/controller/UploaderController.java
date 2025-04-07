package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.service.UploaderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/uploader")
public class UploaderController {

    @Autowired
    private UploaderService uploaderService;


    @GetMapping
    public String showUsers(Model model) {
        model.addAttribute("uploader", uploaderService.getAllUsers());
        return "uploader";
    }

    @PostMapping("/add")
    public String addUser(@RequestParam String name,
                          @RequestParam String email,
                          @RequestParam int tickets,
                          @RequestParam boolean paid) {
        uploaderService.addUser(name, email, tickets, paid);
        return "redirect:/uploader";
    }

    @PostMapping("/upload")
    public String uploadUsers(@RequestParam("file") MultipartFile file) throws IOException {
        uploaderService.uploadUsersFromFile(file);
        return "redirect:/uploader";
    }

}
