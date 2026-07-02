package com.travelgenie.controller;

import com.travelgenie.model.ChatMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatController {

    // ==========================================
    // 📡 1. WEBSOCKET REAL-TIME MESSAGING LOGIC
    // ==========================================

    // 🎯 යූසර් හෝ ඇඩ්මින් ලයිව් චැට් රූම් එකකට මැසේජ් එකක් යැව්වාම ක්‍රියාත්මක වෙනවා
    @MessageMapping("/chat/{sessionId}/sendMessage")
    @SendTo("/topic/chat/{sessionId}")
    public ChatMessage sendMessage(@DestinationVariable String sessionId, @Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    // 🎯 චැට් එකට අලුතෙන් සම්බන්ධ වෙද්දී (Join වෙද්දී) ක්‍රියාත්මක වෙනවා
    @MessageMapping("/chat/{sessionId}/addUser")
    @SendTo("/topic/chat/{sessionId}")
    public ChatMessage addUser(@DestinationVariable String sessionId, @Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        if (headerAccessor.getSessionAttributes() != null) {
            headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
            headerAccessor.getSessionAttributes().put("sessionId", sessionId);
        }
        return chatMessage;
    }

    // ==========================================
    // 🌐 2. THYMELEAF VIEW PAGE MAPPINGS
    // ==========================================

    // 👤 කස්ටමර්ට පේන Live Support පිටුව ඕපන් කර ගැනීමට
    @GetMapping("/support")
    public String showCustomerSupportPage() {
        return "support"; // templates/support.html එක රෙන්ඩර් කරයි
    }

    // 👑 ඇඩ්මින්ට පේන Live Support Dashboard පිටුව ඕපන් කර ගැනීමට
    @GetMapping("/admin/support")
    public String showAdminSupportPage() {
        return "admin-chat"; // templates/admin-chat.html එක රෙන්ඩර් කරයි
    }
}
