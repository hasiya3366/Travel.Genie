package com.example.TravelApp.service;

import com.example.TravelApp.model.TravelPlanRequest;
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

    private final String API_KEY = "AQ.Ab8RN6ILXXMsAA7Je3sKIx1yEtFNtGgHfrhe7LyxRPOyxTAExA";
    private final String API_URL = "https://generativelanguage.googleapis.com/v1/models/gemini-2.5-flash:generateContent?key=" + API_KEY;

    public String getSmartItinerary(TravelPlanRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        String userInput = request.getDestination() != null ? request.getDestination().trim() : "";
        String cleanInput = userInput.toLowerCase();

        if (cleanInput.equals("hi") || cleanInput.equals("hello") || cleanInput.equals("hey")) {
            httpSession.invalidate();
            return "Greetings from Travel Genie! I am your Premium Expedition Planner. How may I assist you in crafting your bespoke Sri Lankan itinerary today? Please provide your desired destination, duration, or budget preference to begin.";
        }

        String state = (httpSession.getAttribute("chat_state") != null) ? httpSession.getAttribute("chat_state").toString() : "CASUAL";

        if (state.equals("CASUAL") && (cleanInput.contains("plan") || cleanInput.contains("trip") || cleanInput.contains("tour") || cleanInput.contains("travel") || cleanInput.contains("itinerary"))) {
            state = "ASK_DESTINATION";
            httpSession.setAttribute("chat_state", state);
            return "Hey brother! Awesome, let's plan a killer trip. First things first, where in Sri Lanka do you want to go?";
        }

        if (state.equals("ASK_DESTINATION")) {
            httpSession.setAttribute("saved_destination", userInput);
            httpSession.setAttribute("chat_state", "ASK_DURATION");
            return "Got it! Next, for how many days are you planning this adventure? (e.g., 3, 4, 7)";
        }
        else if (state.equals("ASK_DURATION")) {
            httpSession.setAttribute("saved_duration", userInput);
            httpSession.setAttribute("chat_state", "ASK_COMPANION");
            return "Perfect. Who are you travelling with? (e.g., Solo, with Friends, or Family?)";
        }
        else if (state.equals("ASK_COMPANION")) {
            httpSession.setAttribute("saved_companion", userInput);
            httpSession.setAttribute("chat_state", "ASK_BUDGET");
            return "Meticulous details. Finally, what is your preferred budget scale? (Low, Medium, or Luxury?)";
        }
        else if (state.equals("ASK_BUDGET")) {
            String dest = httpSession.getAttribute("saved_destination").toString();
            String duration = httpSession.getAttribute("saved_duration").toString();
            String companion = httpSession.getAttribute("saved_companion").toString();
            String budget = userInput;

            httpSession.invalidate();

            String fullPrompt = "You are a friendly, highly concise travel assistant for Travel Genie.\n\n"
                    + "STRICT RULES:\n"
                    + "1. MAXIMUM LENGTH: Your entire response must NOT exceed 150-200 words total.\n"
                    + "2. STRICT NEWLINE FORMATTING: You MUST start each day on a completely NEW LINE (using a line break \\n). Format exactly like this with clean line breaks:\n\n"
                    + "Day 1: [Morning activity] -> [Afternoon activity] -> [Evening activity] -> [Night activity]\n"
                    + "Day 2: [Morning activity] -> [Afternoon activity] -> [Evening activity] -> [Night activity]\n\n"
                    + String.format("Request Specifics: Provide an itinerary for %s days in %s for %s on a %s budget framework. Ensure each day begins on a fresh new line.",
                    duration, dest, companion, budget);

            return callGeminiAPI(restTemplate, fullPrompt);
        }

        String casualPrompt = "You are a friendly, supportive, and brotherly travel assistant for Travel Genie. "
                + "The user is just chatting casually, talking about emotions, or asking random things. Respond warmly, supportively, and very briefly (under 80 words) like a helpful peer. Do NOT give any structured travel day-by-day plans or ask for destinations here.\n\n"
                + "User Message: " + userInput;

        return callGeminiAPI(restTemplate, casualPrompt);
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
            ResponseEntity<Map> response = restTemplate.postForEntity(API_URL, entity, Map.class);
            List<Map> candidates = (List<Map>) response.getBody().get("candidates");
            Map content = (Map) candidates.get(0).get("content");
            List<Map> parts = (List<Map>) content.get("parts");
            return parts.get(0).get("text").toString();
        } catch (Exception e) {
            return "An unexpected operational error occurred while processing your request: " + e.getMessage();
        }
    }
}
