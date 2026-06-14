package com.arjun.crm.service;

import com.arjun.crm.dto.response.NotificationResponse;
import com.arjun.crm.dto.response.NotificationSummaryResponse;
import com.arjun.crm.entity.User;
import com.arjun.crm.enums.NotificationType;
import com.arjun.crm.enums.ReferenceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {
    
    /**
     * Create a notification
     */
    NotificationResponse createNotification(
            User recipient,
            String title,
            String message,
            NotificationType type,
            Long referenceId,
            ReferenceType referenceType
    );
    
    /**
     * Get notifications for current user
     */
    Page<NotificationResponse> getNotifications(Pageable pageable);
    
    /**
     * Get unread notifications for current user
     */
    Page<NotificationResponse> getUnreadNotifications(Pageable pageable);
    
    /**
     * Get notification summary (unread count)
     */
    NotificationSummaryResponse getNotificationSummary();
    
    /**
     * Mark notification as read
     */
    NotificationResponse markAsRead(Long notificationId);
    
    /**
     * Mark all notifications as read for current user
     */
    int markAllAsRead();
    
    /**
     * Delete notification
     */
    void deleteNotification(Long notificationId);
    
    /**
     * Cleanup old read notifications
     */
    int cleanupOldNotifications(int daysOld);
}
