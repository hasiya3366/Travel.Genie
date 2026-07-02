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
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 🌐 setAllowedOriginPatterns("*") එක දැම්මම ඕනෑම ලයිව් URL එකකින් එන කනෙක්ෂන් සර්වර් එකෙන් බාරගන්නවා මචං!
        registry.addEndpoint("/ws-support")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}
