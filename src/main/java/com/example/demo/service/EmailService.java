package com.example.demo.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmailWithQRCode(String to, String name, String qrCodeBase64) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject("Your QR Code Ticket");

        String htmlContent = "<h2>Hello, " + name + "!</h2>"
                + "<p>Here is your QR code for the event:</p>"
                + "<img src='data:image/png;base64," + qrCodeBase64 + "' alt='QR Code'>"
                + "<p>Scan this QR code to check in.</p>";

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
}
