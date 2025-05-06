package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NavigationController {

    // Handle "How To" page route
    @GetMapping("/howto")
    public String howToPage() {
        return "howto";
    }

    // Handle "Contact" page route
    @GetMapping("/contact")
    public String contactPage() {
        return "contact";
    }
}
