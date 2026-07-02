package com.travelgenie.model;

public class ChatMessage {
    private String sessionId;
    private String sender;
    private String content;
    private MessageType type;

    public enum MessageType {
        CHAT, JOIN, LEAVE
    }

    // 💡 Getters and Setters (IntelliJ එකෙන් Alt+Insert ගහලා ඉබේම Generate කරගන්න මචං)
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public MessageType getType() { return type; }
    public void setType(MessageType type) { this.type = type; }
}
