// package com.example.TravelApp.controller;

// import com.example.TravelApp.model.TravelPlanRequest;
// import com.example.TravelApp.service.ChatBotService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// @RestController
// @RequestMapping("/api/chatbot")
// @CrossOrigin(origins = "*")
// public class ChatBotController {

//     @Autowired
//     private ChatBotService chatBotService;

//     @PostMapping("/generate-itinerary")
//     public ResponseEntity<String> generateItinerary(@RequestBody TravelPlanRequest request) {
//         String itinerary = chatBotService.getSmartItinerary(request);
//         return ResponseEntity.ok(itinerary);
//     }
// }
