package com.example.demo.service;


import com.example.demo.model.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class UploaderService {

    @Autowired
    private UserRepository userRepository;


    public void addUser(String name, String email, int tickets, boolean paid) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setTickets(tickets);
        user.setPaid(paid);
        userRepository.save(user);
    }


    public void uploadUsersFromFile(MultipartFile file) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            if (data.length == 4) {
                User user = new User();
                user.setName(data[0].trim());
                user.setEmail(data[1].trim());
                user.setTickets(Integer.parseInt(data[2].trim()));
                user.setPaid(Boolean.parseBoolean(data[3].trim()));
                userRepository.save(user);
            }
        }
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}


