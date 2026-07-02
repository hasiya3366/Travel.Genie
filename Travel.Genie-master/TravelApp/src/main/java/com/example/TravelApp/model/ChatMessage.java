package com.example.TravelApp.model;

public class ChatMessage {
    private String sender;
    private String content;
    private MessageType type;
    private String sessionId;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }

    // --- GETTERS & SETTERS ---
    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public MessageType getType() { return type; }
    public void setType(MessageType type) { this.type = type; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
}
