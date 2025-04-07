package com.example.demo.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String name;
    private String email;
    private boolean paid;
    private int tickets;

    public User() {
    }

    public User(String name, String email, boolean paid, int tickets) {
        this.name = name;
        this.email = email;
        this.paid = paid;
        this.tickets = tickets;
    }

}
