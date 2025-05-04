package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

}
