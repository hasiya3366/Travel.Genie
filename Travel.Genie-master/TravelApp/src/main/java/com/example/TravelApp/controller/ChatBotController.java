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
package com.example.TravelApp.controller;

import com.example.TravelApp.model.TravelPlanRequest;
import com.example.TravelApp.service.ChatBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin(origins = "*") // Front-end එක කොහේ තිබ්බත් CORS Error එන එක වළක්වන්න
public class ChatBotController {

    @Autowired
    private ChatBotService chatBotService;

    @PostMapping("/generate-itinerary")
    public ResponseEntity<String> generateItinerary(@RequestBody TravelPlanRequest request) {
        // Front-end එකෙන් එන JSON එක TravelPlanRequest object එකට auto-map වෙනවා.
        // එතනින් destination (උදා: "Kandy") එක විතරක් අරන් ChatBotService එකට pass කරනවා.
        String itinerary = chatBotService.getSmartItinerary(request.getDestination());
        
        // Gemini API එකෙන් ආපු පිළිතුර (Itinerary text එක) front-end එකට යවනවා.
        return ResponseEntity.ok(itinerary);
    }
}
