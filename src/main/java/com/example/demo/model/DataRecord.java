package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "DATA_RECORD")
public class DataRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int numberOfTickets;
    private boolean paid;
    private String email;
    @Column(name = "QRCODEDATA")
    private String qrCodeData;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }


    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isPaid() {
        return paid;
    }

    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(int numberOfTickets) {

        this.numberOfTickets = numberOfTickets;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getQrCodeData() {

        return qrCodeData;
    }

    public void setQrCodeData(String qrCodeData) {

        this.qrCodeData = qrCodeData;
    }
}
