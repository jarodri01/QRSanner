package com.example.demo.controller;


import com.example.demo.model.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

@Controller
public class QRCodeVerificationController {

    @Autowired
    private UserRepository repository;

    @PostMapping("/verify-qr")
    public String verifyQRCode(@RequestParam("qrContent") String qrContent, Model model) {
        try {
            // Parse QR Code content
            ObjectMapper mapper = new ObjectMapper();
            User qrRecord = mapper.readValue(qrContent, User.class);

            // Fetch data from the database
            Optional<User> optionalRecord = repository.findById(qrRecord.getId());
            if (!optionalRecord.isPresent()) {
                model.addAttribute("message", "QR Code does not match any record.");
                model.addAttribute("status", "error");
                return "verify";
            }

            User dbRecord = optionalRecord.get();

            // Compare QR data with database data
            if (qrRecord.getName().equals(dbRecord.getName()) &&
                    qrRecord.getEmail().equals(dbRecord.getEmail())) {
                model.addAttribute("message", "Match successful!");
                model.addAttribute("status", "success");
            } else {
                model.addAttribute("message", "QR Code does not match the database record.");
                model.addAttribute("status", "error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "Error occurred during verification.");
            model.addAttribute("status", "error");
        }

        return "verify"; // Return a verification UI
    }
}

