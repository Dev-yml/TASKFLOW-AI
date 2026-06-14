package com.arjun.crm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket Configuration
 * Configures STOMP messaging over WebSocket for real-time communication
 * 
 * CRITICAL: Uses explicit setAllowedOrigins() (NOT patterns, NOT wildcards)
 * to ensure Access-Control-Allow-Credentials: true is properly emitted for SockJS.
 * 
 * Features:
 * - STOMP protocol support
 * - SockJS fallback for browsers without WebSocket support
 * - Topic-based broadcasting
 * - User-specific queues
 * - Proper CORS credentials support
 * 
 * Endpoints:
 * - /ws - WebSocket connection endpoint
 * 
 * Destinations:
 * - /topic/* - Broadcast to all subscribers
 * - /queue/* - Point-to-point messaging
 * - /user/queue/* - User-specific messages
 * 
 * @author CRM Backend Team
 * @version 1.0
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private WebSocketAuthChannelInterceptor webSocketAuthChannelInterceptor;

    /**
     * Configure message broker
     * Sets up in-memory STOMP broker
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Use simple in-memory message broker
        config.enableSimpleBroker("/topic", "/queue");
        
        // Prefix for messages from client to server
        config.setApplicationDestinationPrefixes("/app");
        
        // Prefix for user-specific messages
        config.setUserDestinationPrefix("/user");
    }

    /**
     * Register STOMP endpoints
     * Configures WebSocket connection endpoints with SockJS fallback
     * 
     * CRITICAL: Using setAllowedOrigins() with explicit origins (NOT "*", NOT patterns)
     * ensures Spring automatically sets Access-Control-Allow-Credentials: true
     * which is REQUIRED for SockJS /ws/info endpoint to work properly.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register STOMP endpoint with SockJS fallback
        // MUST use explicit origins for credentials to work
        registry.addEndpoint("/ws")
                .setAllowedOrigins(
                        "http://localhost:3000",
                        "http://127.0.0.1:3000",
                        "http://localhost:5173",
                        "http://127.0.0.1:5173"
                )
                .withSockJS();
    }

    /**
     * Register the JWT channel interceptor so every STOMP CONNECT frame
     * is authenticated and the session Principal is set correctly.
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(webSocketAuthChannelInterceptor);
    }
}
