package com.arjun.crm.controller;

import com.arjun.crm.dto.request.TypingIndicatorRequest;
import com.arjun.crm.dto.response.TypingIndicatorResponse;
import com.arjun.crm.entity.User;
import com.arjun.crm.repository.UserRepository;
import com.arjun.crm.service.OnlinePresenceService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketController {

    private final OnlinePresenceService onlinePresenceService;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    // ─── STOMP session lifecycle events ──────────────────────────────────────

    /**
     * Fires automatically when any STOMP CONNECT frame completes.
     * Marks the user online and broadcasts the FULL online user list to every
     * connected client so newly joined users see who is already online.
     */
    @EventListener
    public void handleConnect(SessionConnectEvent event) {
        User user = getUserFromPrincipal(event.getUser());
        if (user == null) return;

        log.info("STOMP CONNECT — user {} ({})", user.getId(), user.getEmail());
        onlinePresenceService.markUserOnline(user.getId());

        // 1. Tell everyone this user came online
        messagingTemplate.convertAndSend("/topic/presence",
                new PresenceEvent(user.getId(), user.getFullName(), true,
                        onlinePresenceService.getLastSeen(user.getId())));

        // 2. Send the full online list to ALL clients so latecomers are in sync
        Set<Long> onlineUsers = onlinePresenceService.getOnlineUsers();
        messagingTemplate.convertAndSend("/topic/presence/snapshot",
                new PresenceSnapshot(List.copyOf(onlineUsers)));
    }

    /**
     * Fires automatically when a STOMP session disconnects (tab close, network drop, etc.)
     * No need for the client to send an explicit /app/chat/disconnect message.
     */
    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        User user = getUserFromPrincipal(event.getUser());
        if (user == null) return;

        log.info("STOMP DISCONNECT — user {} ({})", user.getId(), user.getEmail());
        onlinePresenceService.markUserOffline(user.getId());

        // Broadcast updated presence to all remaining clients
        messagingTemplate.convertAndSend("/topic/presence",
                new PresenceEvent(user.getId(), user.getFullName(), false,
                        onlinePresenceService.getLastSeen(user.getId())));

        Set<Long> onlineUsers = onlinePresenceService.getOnlineUsers();
        messagingTemplate.convertAndSend("/topic/presence/snapshot",
                new PresenceSnapshot(List.copyOf(onlineUsers)));
    }

    // ─── Heartbeat — refreshes the Redis TTL every 30 s ─────────────────────

    /**
     * Client sends to /app/chat/heartbeat every 30 seconds.
     * Keeps the Redis presence key alive between CONNECT and DISCONNECT.
     */
    @MessageMapping("/chat/heartbeat")
    public void handleHeartbeat(SimpMessageHeaderAccessor headerAccessor) {
        User user = getAuthenticatedUser(headerAccessor);
        if (user == null) return;
        onlinePresenceService.markUserOnline(user.getId()); // resets the TTL
    }

    // ─── Typing indicator ────────────────────────────────────────────────────

    @MessageMapping("/chat/typing")
    public void handleTypingIndicator(@Payload TypingIndicatorRequest request,
                                       SimpMessageHeaderAccessor headerAccessor) {
        User currentUser = getAuthenticatedUser(headerAccessor);
        if (currentUser == null) return;

        onlinePresenceService.setTypingStatus(
                currentUser.getId(), request.getChatRoomId(), request.getIsTyping());

        messagingTemplate.convertAndSend(
                "/topic/chat/" + request.getChatRoomId() + "/typing",
                TypingIndicatorResponse.builder()
                        .chatRoomId(request.getChatRoomId())
                        .userId(currentUser.getId())
                        .userName(currentUser.getFullName())
                        .isTyping(request.getIsTyping())
                        .build());
    }

    // ─── Legacy explicit connect/disconnect (kept for backwards compat) ───────

    @MessageMapping("/chat/connect")
    public void handleExplicitConnect(SimpMessageHeaderAccessor headerAccessor) {
        // No-op: handled by SessionConnectEvent above
    }

    @MessageMapping("/chat/disconnect")
    public void handleExplicitDisconnect(SimpMessageHeaderAccessor headerAccessor) {
        // No-op: handled by SessionDisconnectEvent above
    }

    // ─── Private helpers ─────────────────────────────────────────────────────

    private User getAuthenticatedUser(SimpMessageHeaderAccessor headerAccessor) {
        return getUserFromPrincipal(headerAccessor.getUser());
    }

    private User getUserFromPrincipal(Principal principal) {
        if (principal == null) return null;
        String email;
        if (principal instanceof Authentication auth &&
                auth.getPrincipal() instanceof UserDetails ud) {
            email = ud.getUsername();
        } else {
            email = principal.getName();
        }
        return userRepository.findByEmail(email).orElse(null);
    }

    // ─── Response DTOs ───────────────────────────────────────────────────────

    @Data @AllArgsConstructor
    public static class PresenceEvent {
        private Long userId;
        private String userName;
        private Boolean isOnline;
        private String lastSeen;
    }

    @Data @AllArgsConstructor
    public static class PresenceSnapshot {
        private List<Long> onlineUserIds;
    }
}
