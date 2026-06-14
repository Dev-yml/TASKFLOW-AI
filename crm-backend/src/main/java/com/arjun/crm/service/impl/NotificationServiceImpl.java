package com.arjun.crm.service.impl;

import com.arjun.crm.dto.response.NotificationResponse;
import com.arjun.crm.dto.response.NotificationSummaryResponse;
import com.arjun.crm.entity.Notification;
import com.arjun.crm.entity.User;
import com.arjun.crm.enums.NotificationType;
import com.arjun.crm.enums.ReferenceType;
import com.arjun.crm.exception.AccessDeniedException;
import com.arjun.crm.exception.ResourceNotFoundException;
import com.arjun.crm.repository.NotificationRepository;
import com.arjun.crm.repository.UserRepository;
import com.arjun.crm.service.NotificationService;
import com.arjun.crm.service.WebSocketNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final WebSocketNotificationService webSocketNotificationService;

    @Override
    @Transactional
    public NotificationResponse createNotification(
            User recipient,
            String title,
            String message,
            NotificationType type,
            Long referenceId,
            ReferenceType referenceType) {
        
        log.info("Creating notification for user: {} of type: {}", recipient.getEmail(), type);

        Notification notification = Notification.builder()
                .recipient(recipient)
                .title(title)
                .message(message)
                .type(type)
                .referenceId(referenceId)
                .referenceType(referenceType)
                .isRead(false)
                .build();

        Notification savedNotification = notificationRepository.save(notification);
        NotificationResponse response = NotificationResponse.fromEntity(savedNotification);

        // Send real-time notification via WebSocket
        webSocketNotificationService.sendNotificationToUser(recipient.getId(), response);

        log.info("Notification created with ID: {}", savedNotification.getId());
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponse> getNotifications(Pageable pageable) {
        User currentUser = getAuthenticatedUser();
        log.info("Fetching notifications for user: {}", currentUser.getEmail());

        Page<Notification> notifications = notificationRepository
                .findByRecipientIdOrderByCreatedAtDesc(currentUser.getId(), pageable);

        return notifications.map(NotificationResponse::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponse> getUnreadNotifications(Pageable pageable) {
        User currentUser = getAuthenticatedUser();
        log.info("Fetching unread notifications for user: {}", currentUser.getEmail());

        Page<Notification> notifications = notificationRepository
                .findByRecipientIdAndIsReadFalseOrderByCreatedAtDesc(currentUser.getId(), pageable);

        return notifications.map(NotificationResponse::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationSummaryResponse getNotificationSummary() {
        User currentUser = getAuthenticatedUser();
        
        Long unreadCount = notificationRepository.countByRecipientIdAndIsReadFalse(currentUser.getId());
        Long totalCount = notificationRepository.countByRecipientIdOrderByCreatedAtDesc(currentUser.getId());

        return NotificationSummaryResponse.builder()
                .unreadCount(unreadCount)
                .totalCount(totalCount)
                .build();
    }

    @Override
    @Transactional
    public NotificationResponse markAsRead(Long notificationId) {
        User currentUser = getAuthenticatedUser();
        log.info("Marking notification {} as read by user: {}", notificationId, currentUser.getEmail());

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with ID: " + notificationId));

        // Verify notification belongs to current user
        if (!notification.getRecipient().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can only mark your own notifications as read");
        }

        if (!notification.getIsRead()) {
            notification.setIsRead(true);
            notification.setReadAt(LocalDateTime.now());
            notificationRepository.save(notification);
        }

        return NotificationResponse.fromEntity(notification);
    }

    @Override
    @Transactional
    public int markAllAsRead() {
        User currentUser = getAuthenticatedUser();
        log.info("Marking all notifications as read for user: {}", currentUser.getEmail());

        int updatedCount = notificationRepository.markAllAsReadByRecipientId(currentUser.getId());
        log.info("Marked {} notifications as read", updatedCount);

        return updatedCount;
    }

    @Override
    @Transactional
    public void deleteNotification(Long notificationId) {
        User currentUser = getAuthenticatedUser();
        log.info("Deleting notification {} by user: {}", notificationId, currentUser.getEmail());

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with ID: " + notificationId));

        // Verify notification belongs to current user
        if (!notification.getRecipient().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can only delete your own notifications");
        }

        notificationRepository.delete(notification);
        log.info("Notification deleted: {}", notificationId);
    }

    @Override
    @Transactional
    public int cleanupOldNotifications(int daysOld) {
        log.info("Cleaning up notifications older than {} days", daysOld);
        
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysOld);
        int deletedCount = notificationRepository.deleteOldReadNotifications(cutoffDate);
        
        log.info("Deleted {} old notifications", deletedCount);
        return deletedCount;
    }

    /**
     * Get authenticated user from security context
     */
    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }
}
