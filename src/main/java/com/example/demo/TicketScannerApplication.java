package com.example.demo;

import com.example.demo.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class TicketScannerApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(TicketScannerApplication.class);

        Map<String, Object> props = new HashMap<>();
        String port = System.getenv("PORT");
        if (port != null) {
            props.put("server.port", port);
        }
        app.setDefaultProperties(props);

        app.run(args);
    }

    @Bean
    CommandLineRunner commandLineRunner(UserService userService) {
        return args -> {
            try {
                userService.createUser("admin", "admin");
            } catch (Exception e) {
                // User might already exist
                System.out.println("Admin user already exists");
            }
        };
    }


}
