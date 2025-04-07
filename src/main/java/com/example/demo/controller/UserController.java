package com.example.demo.controller;


import com.example.demo.model.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.service.EmailService;
import com.example.demo.service.QRCodeService;
import com.google.zxing.WriterException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QRCodeService qrCodeService;

    @Autowired
    private EmailService emailService;

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
            String qrCode = qrCodeService.generateQRCode(id);
            emailService.sendEmailWithQRCode(user.get().getEmail(), user.get().getName(), qrCode);
            model.addAttribute("message", "Email sent successfully to " + user.get().getEmail());
            return "email-confirmation";
        } else {
            return "Error: User not found";
        }
    }
}
