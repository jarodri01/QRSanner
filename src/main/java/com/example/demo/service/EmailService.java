package com.example.demo.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.IOException;

import static org.unbescape.html.HtmlEscape.escapeHtml4;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmailWithQRCode(String to, String name, String qrCodeBase64, String logoUrl) throws MessagingException {
        // Validate inputs
        if (to == null || name == null || qrCodeBase64 == null || logoUrl == null) {
            throw new IllegalArgumentException("Email parameters cannot be null");
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        try {
            helper.setTo(to);
            helper.setSubject("Your QR Code Ticket");

            // Use a template engine or at least proper HTML escaping
            String htmlContent = String.format("""
                            <h2>Hello, %s!</h2>
                            <p>Here is your QR code for the event:</p>
                            <img src='data:image/png;base64,%s' alt='QR Code'>
                            <p>Scan this QR code to check in.</p>
                            <img src='cid:logoImage' alt='Event Logo' style='width:150px;height:auto;'>""",
                    escapeHtml4(name),  // Using Apache Commons Text for HTML escaping
                    qrCodeBase64
            );

            helper.setText(htmlContent, true);

            // Use URLResource instead of ClassPathResource for external URLs
            Resource logoResource = new UrlResource(logoUrl);
            if (!logoResource.exists()) {
                throw new MessagingException("Logo resource not found: " + logoUrl);
            }
            helper.addInline("logoImage", logoResource);

            mailSender.send(message);
        } catch (IOException e) {
            throw new MessagingException("Failed to load logo resource", e);
        }
    }

}
