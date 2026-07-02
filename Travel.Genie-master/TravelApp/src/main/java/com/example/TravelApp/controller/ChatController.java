package com.travelgenie.controller;

import com.travelgenie.model.ChatMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    // 🎯 යූසර් කෙනෙක් හෝ ඇඩ්මින් කෙනෙක් ලයිව් චැට් රූම් එකකට මැසේජ් එකක් යැව්වාම ක්‍රියාත්මක වෙනවා
    @MessageMapping("/chat/{sessionId}/sendMessage")
    @SendTo("/topic/chat/{sessionId}")
    public ChatMessage sendMessage(@DestinationVariable String sessionId, @Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    // 🎯 චැට් එකට අලුතෙන් සම්බන්ධ වෙද්දී (Join වෙද්දී) ක්‍රියාත්මක වෙනවා
    @MessageMapping("/chat/{sessionId}/addUser")
    @SendTo("/topic/chat/{sessionId}")
    public ChatMessage addUser(@DestinationVariable String sessionId, @Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        // සෙෂන් එක ඇතුළේ යූසර්ගේ නම තියාගන්නවා
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        headerAccessor.getSessionAttributes().put("sessionId", sessionId);
        return chatMessage;
    }
}
