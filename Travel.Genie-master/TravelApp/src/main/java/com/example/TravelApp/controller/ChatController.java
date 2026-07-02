package com.example.TravelApp.controller;

import com.example.TravelApp.model.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import java.util.Map;
import java.util.HashMap;

@Controller
public class ChatController {

    @Autowired
    private com.example.TravelApp.repository.UserRepository userRepository;

    @Autowired
    private com.example.TravelApp.repository.BookingRepository bookingRepository;

    @Autowired
    private com.example.TravelApp.repository.DestinationRepository destinationRepository;

    @Autowired
    private com.example.TravelApp.repository.TourPackageRepository tourPackageRepository;

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

    @MessageMapping("/chat/globalQueue")
    @SendTo("/topic/global-queue")
    @ResponseBody
    public String broadcastGlobalQueue(String ticketJson) {
        return ticketJson;
    }

    @GetMapping("/support")
    public String showCustomerSupportPage(HttpSession session, Model model) {
        Object loggedUser = session.getAttribute("username");
        
        if (loggedUser == null) {
            loggedUser = session.getAttribute("user");
        }

        if (loggedUser != null) {
            if (loggedUser instanceof com.example.TravelApp.model.User) {
                com.example.TravelApp.model.User u = (com.example.TravelApp.model.User) loggedUser;
                model.addAttribute("realName", u.getName());
            } else {
                model.addAttribute("realName", loggedUser.toString());
            }
        } else {
            model.addAttribute("realName", null);
        }
        
        return "support"; 
    }

    @GetMapping("/admin/support")
    public String showAdminSupportPage() {
        return "admin-chat"; 
    }

    @GetMapping("/admin/api/dashboard-stats")
    @ResponseBody
    public Map<String, Object> getRealDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        long totalUsers = userRepository.count();
        long totalBookings = bookingRepository.count();
        long totalDestinations = destinationRepository.count();
        long totalPackages = tourPackageRepository.count();
        
        double totalRevenue = bookingRepository.getTotalRevenue(); 

        stats.put("totalUsers", totalUsers);
        stats.put("totalDestinations", totalDestinations);
        stats.put("totalPackages", totalPackages);
        stats.put("totalBookings", totalBookings);
        stats.put("totalRevenue", totalRevenue);
        
        return stats;
    }
}
