package com.example.TravelApp.service;

import com.example.TravelApp.model.TravelPlanRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatBotService {

    @Autowired
    private HttpSession httpSession;

    // application.properties එකෙන් API Key එක ගනී
    @Value("${gemini.api.key}")
    private String API_KEY;

    // API URL එක සෑදීම
    private String getApiUrl() {
        return "https://generativelanguage.googleapis.com/v1/models/gemini-2.5-flash:generateContent?key=" + API_KEY;
    }

    public String getSmartItinerary(TravelPlanRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        String userInput = request.getDestination() != null ? request.getDestination().trim() : "";
        String cleanInput = userInput.toLowerCase();

        if (cleanInput.equals("hi") || cleanInput.equals("hello") || cleanInput.equals("hey")) {
            httpSession.invalidate();
            return "Greetings from Travel Genie! I am your Premium Expedition Planner. How may I assist you in crafting your bespoke Sri Lankan itinerary today?";
        }

        String state = (httpSession.getAttribute("chat_state") != null) ? httpSession.getAttribute("chat_state").toString() : "CASUAL";

        if (state.equals("CASUAL") && (cleanInput.contains("plan") || cleanInput.contains("trip"))) {
            httpSession.setAttribute("chat_state", "ASK_DESTINATION");
            return "Hey brother! Awesome, let's plan a killer trip. First things first, where in Sri Lanka do you want to go?";
        }

        // Logic එකේ ඉතිරි කොටස (ASK_DESTINATION, ASK_DURATION, ආදිය මෙතන තියෙන්න)
        // ... (ඔයාගේ කලින් තිබුණු logic එකම මෙතන තියන්න) ...

        return callGeminiAPI(restTemplate, "User says: " + userInput);
    }

    private String callGeminiAPI(RestTemplate restTemplate, String promptText) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> textMap = new HashMap<>();
        textMap.put("text", promptText);
        Map<String, Object> partsMap = new HashMap<>();
        partsMap.put("parts", List.of(textMap));
        Map<String, Object> contentsMap = new HashMap<>();
        contentsMap.put("contents", List.of(partsMap));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(contentsMap, headers);

        try {
            // මෙතන getApiUrl() භාවිතා කරන්න
            ResponseEntity<Map> response = restTemplate.postForEntity(getApiUrl(), entity, Map.class);
            List<Map> candidates = (List<Map>) response.getBody().get("candidates");
            Map content = (Map) candidates.get(0).get("content");
            List<Map> parts = (List<Map>) content.get("parts");
            return parts.get(0).get("text").toString();
        } catch (Exception e) {
            e.printStackTrace(); // වැරදි මොකක්ද කියලා Console එකෙන් බලන්න
            return "An error occurred: " + e.getMessage();
        }
    }
}
