package com.example.demo.controller;


import com.example.demo.model.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.service.EmailService;
import com.example.demo.service.QRCodeService;
import com.google.zxing.WriterException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;

    private final QRCodeService qrCodeService;

    private final EmailService emailService;

    @Autowired
    public UserController(UserRepository userRepository, QRCodeService qrCodeService, EmailService emailService) {
        this.userRepository = userRepository;
        this.qrCodeService = qrCodeService;
        this.emailService = emailService;
    }

    @GetMapping("/{id}")
    public String getUserDetails(@PathVariable Long id, Model model) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "user-details";
        } else {
            return "error";
        }
    }

    @GetMapping("/generate-qr/{id}")
    public String generateQRCode(@PathVariable Long id, HttpServletResponse response, Model model) throws WriterException, IOException {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            String qrCode = qrCodeService.generateQRCode(id);
            model.addAttribute("qrCode", qrCode);
            model.addAttribute("user", user.get());
            return "qr-page";
        } else {
            return "error";
        }

    }

    @PostMapping("/send-qr/{id}")
    public String sendQRCodeEmail(@PathVariable Long id, Model model) throws WriterException, IOException, MessagingException {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User currentUser = user.get();
            String qrCode = qrCodeService.generateQRCode(id);
            String logoUrl = "https://res.cloudinary.com/dcehvbp8e/image/upload/v1746408808/Logo_Melrose_ivlz3i.png";

            emailService.sendEmailWithQRCode(currentUser.getEmail(), currentUser.getName(), qrCode, logoUrl);
            model.addAttribute("message", "Email sent successfully to " + currentUser.getEmail());
            return "email-confirmation";
        } else {
            model.addAttribute("error", "User not found");
            return "error";
        }
    }

}
