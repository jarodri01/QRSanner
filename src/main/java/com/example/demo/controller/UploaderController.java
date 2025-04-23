package com.example.demo.controller;


import com.example.demo.service.UploaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;


@Controller
@RequestMapping("/index")
public class UploaderController {

    @Autowired
    private UploaderService uploaderService;


    @GetMapping
    public String showUsers(Model model) {
        model.addAttribute("index", uploaderService.getAllUsers());
        return "index";
    }

    @PostMapping("/add")
    public String addUser(@RequestParam String name,
                          @RequestParam String email,
                          @RequestParam int tickets,
                          @RequestParam boolean paid) {
        uploaderService.addUser(name, email, tickets, paid);
        return "redirect:/index";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoll(@PathVariable Long id) {
        try {
            boolean isDeleted = uploaderService.deleteUserById(id);
            if (isDeleted) {
                return ResponseEntity.ok("Roll deleted successfully.");
            } else {
                return ResponseEntity.status(404).body("Roll not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while deleting the roll.");
        }
    }


    @PostMapping("/upload")
    public String uploadUsers(@RequestParam("file") MultipartFile file) throws IOException {
        uploaderService.uploadUsersFromFile(file);
        return "redirect:/index";
    }

}
