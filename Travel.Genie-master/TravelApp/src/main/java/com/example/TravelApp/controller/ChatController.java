// package com.example.TravelApp.controller;

// import com.example.TravelApp.model.ChatMessage;
// import org.springframework.messaging.handler.annotation.DestinationVariable;
// import org.springframework.messaging.handler.annotation.MessageMapping;
// import org.springframework.messaging.handler.annotation.Payload;
// import org.springframework.messaging.handler.annotation.SendTo;
// import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.ResponseBody;

// import jakarta.servlet.http.HttpSession;

// @Controller
// public class ChatController {

//     @MessageMapping("/chat/{sessionId}/sendMessage")
//     @SendTo("/topic/chat/{sessionId}")
//     public ChatMessage sendMessage(@DestinationVariable String sessionId, @Payload ChatMessage chatMessage) {
//         return chatMessage;
//     }

//     @MessageMapping("/chat/{sessionId}/addUser")
//     @SendTo("/topic/chat/{sessionId}")
//     public ChatMessage addUser(@DestinationVariable String sessionId, @Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
//         if (headerAccessor.getSessionAttributes() != null) {
//             headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
//             headerAccessor.getSessionAttributes().put("sessionId", sessionId);
//         }
//         return chatMessage;
//     }

//     @MessageMapping("/chat/globalQueue")
//     @SendTo("/topic/global-queue")
//     @ResponseBody
//     public String broadcastGlobalQueue(String ticketJson) {
//         return ticketJson;
//     }

//     @GetMapping("/support")
//     public String showCustomerSupportPage(HttpSession session, Model model) {
//         Object loggedUser = session.getAttribute("username");
        
//         if (loggedUser == null) {
//             loggedUser = session.getAttribute("user");
//         }

//         if (loggedUser != null) {
//             if (loggedUser instanceof com.example.TravelApp.model.User) {
//                 com.example.TravelApp.model.User u = (com.example.TravelApp.model.User) loggedUser;
//                 model.addAttribute("realName", u.getName());
//             } else {
//                 model.addAttribute("realName", loggedUser.toString());
//             }
//         } else {
//             model.addAttribute("realName", null);
//         }
        
//         return "support"; 
//     }

//     @GetMapping("/admin/support")
//     public String showAdminSupportPage() {
//         return "admin-chat"; 
//     }
// }
package com.example.TravelApp.controller;

import com.example.TravelApp.model.ChatMessage;
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
    public Map<String, Object> getDummyStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", 10);
        stats.put("totalDestinations", 5);
        stats.put("totalPackages", 8);
        stats.put("totalBookings", 3);
        stats.put("totalRevenue", 150000.00);
        return stats;
    }
}
