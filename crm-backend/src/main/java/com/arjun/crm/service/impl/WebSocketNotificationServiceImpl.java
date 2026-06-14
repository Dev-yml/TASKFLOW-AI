package com.arjun.crm.service.impl;

import com.arjun.crm.dto.response.NotificationResponse;
import com.arjun.crm.service.WebSocketNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketNotificationServiceImpl implements WebSocketNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendNotificationToUser(Long userId, NotificationResponse notification) {
        log.info("Sending WebSocket notification to user: {}", userId);
        
        try {
            // Send to user-specific queue
            messagingTemplate.convertAndSendToUser(
                    userId.toString(),
                    "/queue/notifications",
                    notification
            );
            log.info("WebSocket notification sent successfully to user: {}", userId);
        } catch (Exception e) {
            log.error("Failed to send WebSocket notification to user: {}", userId, e);
        }
    }

    @Override
    public void broadcastNotification(NotificationResponse notification) {
        log.info("Broadcasting WebSocket notification to all users");
        
        try {
            // Broadcast to all subscribers
            messagingTemplate.convertAndSend("/topic/notifications", notification);
            log.info("WebSocket notification broadcasted successfully");
        } catch (Exception e) {
            log.error("Failed to broadcast WebSocket notification", e);
        }
    }
}
