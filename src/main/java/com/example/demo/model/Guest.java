package com.example.demo.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.*;



@Entity
@Table(name = "guest")
public class Guest {
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

    public Guest() {

    }

    public Guest(String name, String email, String teacherName, String guestName1, String guestName2, String guestName3, String guestName4) {
        this.name = name;
        this.email = email;
        this.teacherName = teacherName;
        this.guestName1 = guestName1;
        this.guestName2 = guestName2;
        this.guestName3 = guestName3;
        this.guestName4 = guestName4;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getGuestName1() {
        return guestName1;
    }

    public void setGuestName1(String guestName1) {
        this.guestName1 = guestName1;
    }

    public String getGuestName2() {
        return guestName2;
    }

    public void setGuestName2(String guestName2) {
        this.guestName2 = guestName2;
    }

    public String getGuestName3() {
        return guestName3;
    }

    public void setGuestName3(String guestName3) {
        this.guestName3 = guestName3;
    }

    public String getGuestName4() {
        return guestName4;
    }

    public void setGuestName4(String guestName4) {
        this.guestName4 = guestName4;
    }
}
