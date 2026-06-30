// package com.example.TravelApp.service;

// import com.example.TravelApp.model.TravelPlanRequest;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;
// import org.springframework.web.client.RestTemplate;
// import org.springframework.http.*;
// import jakarta.servlet.http.HttpSession;
// import org.springframework.beans.factory.annotation.Autowired;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;

// @Service
// public class ChatBotService {

//     @Autowired
//     private HttpSession httpSession;

//     // application.properties එකෙන් API Key එක ගනී
//     @Value("${gemini.api.key}")
//     private String API_KEY;

//     // API URL එක සෑදීම
//     private String getApiUrl() {
//         return "https://generativelanguage.googleapis.com/v1/models/gemini-2.5-flash:generateContent?key=" + API_KEY;
//     }

//     public String getSmartItinerary(TravelPlanRequest request) {
//         RestTemplate restTemplate = new RestTemplate();
//         String userInput = request.getDestination() != null ? request.getDestination().trim() : "";
//         String cleanInput = userInput.toLowerCase();

//         if (cleanInput.equals("hi") || cleanInput.equals("hello") || cleanInput.equals("hey")) {
//             httpSession.invalidate();
//             return "Greetings from Travel Genie! I am your Premium Expedition Planner. How may I assist you in crafting your bespoke Sri Lankan itinerary today?";
//         }

//         String state = (httpSession.getAttribute("chat_state") != null) ? httpSession.getAttribute("chat_state").toString() : "CASUAL";

//         if (state.equals("CASUAL") && (cleanInput.contains("plan") || cleanInput.contains("trip"))) {
//             httpSession.setAttribute("chat_state", "ASK_DESTINATION");
//             return "Hey brother! Awesome, let's plan a killer trip. First things first, where in Sri Lanka do you want to go?";
//         }

//         // Logic එකේ ඉතිරි කොටස (ASK_DESTINATION, ASK_DURATION, ආදිය මෙතන තියෙන්න)
//         // ... (ඔයාගේ කලින් තිබුණු logic එකම මෙතන තියන්න) ...

//         return callGeminiAPI(restTemplate, "User says: " + userInput);
//     }

//     private String callGeminiAPI(RestTemplate restTemplate, String promptText) {
//         HttpHeaders headers = new HttpHeaders();
//         headers.setContentType(MediaType.APPLICATION_JSON);

//         Map<String, Object> textMap = new HashMap<>();
//         textMap.put("text", promptText);
//         Map<String, Object> partsMap = new HashMap<>();
//         partsMap.put("parts", List.of(textMap));
//         Map<String, Object> contentsMap = new HashMap<>();
//         contentsMap.put("contents", List.of(partsMap));

//         HttpEntity<Map<String, Object>> entity = new HttpEntity<>(contentsMap, headers);

//         try {
//             // මෙතන getApiUrl() භාවිතා කරන්න
//             ResponseEntity<Map> response = restTemplate.postForEntity(getApiUrl(), entity, Map.class);
//             List<Map> candidates = (List<Map>) response.getBody().get("candidates");
//             Map content = (Map) candidates.get(0).get("content");
//             List<Map> parts = (List<Map>) content.get("parts");
//             return parts.get(0).get("text").toString();
//         } catch (Exception e) {
//             e.printStackTrace(); // වැරදි මොකක්ද කියලා Console එකෙන් බලන්න
//             return "An error occurred: " + e.getMessage();
//         }
//     }
// }
package com.example.TravelApp.service;

import com.example.TravelApp.model.TravelPlanRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

@Service
public class ChatBotService {

    // application.properties එකෙන් Gemini API Key එක මෙතනට කියවනවා
    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getSmartItinerary(TravelPlanRequest request) {
        // Gemini API URL එක (v1beta හෝ v1 사용할 수 있습니다)
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey;

        // User එවපු ඩේටා ටික එකතු කරලා AI එකට ලස්සන Prompt එකක් හදනවා
        String userPrompt = String.format(
            "Act as an expert tour guide in Sri Lanka. Generate a detailed travel itinerary for a trip to %s. " +
            "Duration: %d days. Budget level: %s. Travelling season: %s. Companion type: %s. " +
            "Provide everyday highlights, top attractions, and local travel tips. Keep the formatting clean.",
            request.getDestination(), request.getDurationDays(), request.getBudget(), request.getSeason(), request.getCompanionType()
        );

        try {
            // Gemini API එක බලාපොරොත්තු වන නිවැරදි JSON Payload Structure එක සකස් කිරීම
            Map<String, Object> textMap = new HashMap<>();
            textMap.put("text", userPrompt);

            Map<String, Object> partsMap = new HashMap<>();
            partsMap.put("parts", Collections.singletonList(textMap));

            Map<String, Object> contentsMap = new HashMap<>();
            contentsMap.put("contents", Collections.singletonList(partsMap));

            // HTTP Headers සකස් කිරීම
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(contentsMap, headers);

            // POST Request එක Gemini එකට යැවීම
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                // Gemini JSON response එක ඇතුළෙන් text එක විතරක් කඩා ගැනීම
                List candidates = (List) response.getBody().get("candidates");
                Map firstCandidate = (Map) candidates.get(0);
                Map content = (Map) firstCandidate.get("content");
                List parts = (List) content.get("parts");
                Map firstPart = (Map) parts.get(0);
                
                return (String) firstPart.get("text");
            }
            
            return "Sorry, I couldn't generate the itinerary at this moment.";

        } catch (Exception e) {
            e.printStackTrace();
            return "Operational error occurred while processing your request: " + e.getMessage();
        }
    }
}
