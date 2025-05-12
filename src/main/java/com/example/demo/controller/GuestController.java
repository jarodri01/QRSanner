package com.example.demo.controller;


import com.example.demo.model.Guest;

import com.example.demo.repositories.GuestRepository;

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
@RequestMapping("/guest")
public class GuestController {

    private final GuestRepository guestRepository;

    private final QRCodeService qrCodeService;

    private final EmailService emailService;

    @Autowired
    public GuestController(GuestRepository guestRepository, QRCodeService qrCodeService, EmailService emailService) {
        this.guestRepository = guestRepository;
        this.qrCodeService = qrCodeService;
        this.emailService = emailService;
    }

    @GetMapping("/{id}")
    public String getGuestDetails(@PathVariable Long id, Model model) {
        Optional<Guest> guest = guestRepository.findById(id);
        if (guest.isPresent()) {
            model.addAttribute("guest", guest.get());
            return "user-details";
        } else {
            return "error";
        }
    }

    @GetMapping("/generate-qr/{id}")
    public String generateQRCode(@PathVariable Long id, HttpServletResponse response, Model model) throws WriterException, IOException {
        Optional<Guest> guest = guestRepository.findById(id);
        if (guest.isPresent()) {
            String qrCode = qrCodeService.generateQRCode(id);
            model.addAttribute("qrCode", qrCode);
            model.addAttribute("guest", guest.get());
            return "qr-page";
        } else {
            return "error";
        }

    }


    @PostMapping("/send-qr/{id}")
    public String sendQRCodeEmail(@PathVariable Long id, Model model) throws WriterException, IOException, MessagingException {
        Optional<Guest> guest = guestRepository.findById(id);
        if (guest.isPresent()) {
            Guest currentGuest = guest.get();
            String qrCode = qrCodeService.generateQRCode(id);
            String logoUrl = "https://res.cloudinary.com/dcehvbp8e/image/upload/v1746408808/Logo_Melrose_ivlz3i.png";

            emailService.sendEmailWithQRCode(currentGuest.getEmail(), currentGuest.getName(), qrCode, logoUrl);
            model.addAttribute("message", "Email sent successfully to " + currentGuest.getEmail());
            return "email-confirmation";
        } else {
            model.addAttribute("error", "guest not found");
            return "error";
        }
    }

}
