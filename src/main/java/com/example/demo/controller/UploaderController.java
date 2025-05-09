package com.example.demo.controller;


import com.example.demo.service.UploaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/index")
public class UploaderController {
    @Lazy
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
                          @RequestParam String teacherName,
                          @RequestParam String guestName1,
                          @RequestParam String guestName2,
                          @RequestParam String guestName3,
                          @RequestParam String guestName4
                          // @RequestParam int tickets,
                          // @RequestParam boolean paid)
    ) {
        uploaderService.addUser(name, email, teacherName, guestName1, guestName2, guestName3, guestName4);
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
    public String uploadUsers(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        String fileName = file.getOriginalFilename();

        String contentType = file.getContentType();

        // Debug logging
        System.out.println("Received file: " + fileName);
        System.out.println("Content Type: " + contentType);
        System.out.println("File size: " + file.getSize());

        if (fileName == null || fileName.isEmpty()) {
            model.addAttribute("error", "No file selected");
            return "error-page";
        }

        // Check file extension instead of content type
        String fileExtension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();

        try {
            if (isExcelExtension(fileExtension)) {
                uploaderService.uploadUsersFromExcel(file);
            } else if (isTextExtension(fileExtension)) {
                uploaderService.uploadUsersFromTextFile(file);
            } else {
                model.addAttribute("error", "Invalid file type. Please upload an Excel (.xlsx, .xls) or Text (.txt) file.");
                return "error-page";
            }

            return "redirect:/index";
        } catch (Exception e) {
            model.addAttribute("error", "Error processing file: " + e.getMessage());
            return "error-page";
        }
    }

    private boolean isExcelExtension(String fileExtension) {
        return fileExtension.equals(".xlsx") || fileExtension.equals(".xls");
    }

    private boolean isTextExtension(String fileExtension) {
        return fileExtension.equals(".txt");
    }

}