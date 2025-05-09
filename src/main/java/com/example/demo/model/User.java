package com.example.demo.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString
@EqualsAndHashCode
@Getter
@Setter
@Entity
@Table(name = "Guest")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String teacherName;
    private String guestName1;
    private String guestName2;
    private String guestName3;
    private String guestName4;
    //  private boolean paid;
    // private int tickets;

    public User() {
    }

    public User(String name, String email, String teacherName, String guestName1, String guestName2, String guestName3, String guestName4) {
        this.name = name;
        this.email = email;
        this.teacherName = teacherName;
        this.guestName1 = guestName1;
        this.guestName2 = guestName2;
        this.guestName3 = guestName3;
        this.guestName4 = guestName4;

        // this.paid = paid;
        // this.tickets = tickets;
    }

}
