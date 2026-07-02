package com.example.TravelApp.controller;

import com.example.TravelApp.model.ChatMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ChatController {

    // 1. WEBSOCKET REAL-TIME MESSAGING LOGIC
    
    @MessageMapping("/chat/{sessionId}/sendMessage")
    @SendTo("/topic/chat/{sessionId}")
    public ChatMessage sendMessage(@DestinationVariable String sessionId, @Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat/{sessionId}/addUser")
    @SendTo("/topic/chat/{sessionId}")
    public ChatMessage addUser(@DestinationVariable String sessionId, @Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        if (headerAccessor.getSessionAttributes() != null) {
            headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
            headerAccessor.getSessionAttributes().put("sessionId", sessionId);
        }
        return chatMessage;
    }

    // 🎯 🌐 REAL-TIME GLOBAL QUEUE ROUTER (කස්ටමර්ගේ ටිකට් එක ඇඩ්මින් පැනල් එකට ලයිව් යවන තැන)
    @MessageMapping("/chat/globalQueue")
    @SendTo("/topic/global-queue")
    @ResponseBody
    public String broadcastGlobalQueue(String ticketJson) {
        return ticketJson;
    }

    // 2. THYMELEAF VIEW PAGE MAPPINGS

    @GetMapping("/support")
    public String showCustomerSupportPage() {
        return "support"; 
    }

    @GetMapping("/admin/support")
    public String showAdminSupportPage() {
        return "admin-chat"; 
    }
}
