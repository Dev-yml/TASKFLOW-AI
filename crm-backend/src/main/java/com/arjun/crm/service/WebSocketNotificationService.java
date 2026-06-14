package com.arjun.crm.service;

import com.arjun.crm.dto.response.NotificationResponse;

public interface WebSocketNotificationService {
    
    /**
     * Send notification to specific user via WebSocket
     */
    void sendNotificationToUser(Long userId, NotificationResponse notification);
    
    /**
     * Broadcast notification to all connected users
     */
    void broadcastNotification(NotificationResponse notification);
}
