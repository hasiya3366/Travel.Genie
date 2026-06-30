package com.example.TravelApp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contact_messages")
public class ContactMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    @Column(columnDefinition = "TEXT") // දිගු පණිවිඩ ගබඩා කිරීමට TEXT භාවිතා කරයි
    private String message;

    private LocalDateTime submittedAt = LocalDateTime.now();

    // 💡 1. Default Constructor (JPA එකට අනිවාර්යයි)
    public ContactMessage() {}

    // 💡 2. Controller එකේ 'new ContactMessage(...)' එකට අවශ්‍ය Constructor එක
    public ContactMessage(String name, String email, String message) {
        this.name = name;
        this.email = email;
        this.message = message;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
}