// package com.travelgenie.config;
package com.example.TravelApp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 📡 සර්වර් එකෙන් යූසර්ලාට ලයිව් මැසේජ් යවන පාර (Broker)
        config.enableSimpleBroker("/topic");
        
        // 📥 යූසර්ලා සර්වර් එකට මැසේජ් එවද්දී පාවිච්චි කරන මුල් කෑල්ල
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 🔌 Frontend එකෙන් සර්වර් එකට මුලින්ම Connect වෙන Endpoint එක
        registry.addEndpoint("/ws-support").withSockJS();
    }
}
