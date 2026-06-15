package com.arjun.crm.service.impl;

import com.arjun.crm.service.OnlinePresenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory fallback implementation of OnlinePresenceService.
 * Used when Redis is not available (redis.enabled=false).
 * Note: presence state is not shared across instances and resets on restart.
 */
@Service
@ConditionalOnProperty(name = "redis.enabled", havingValue = "false", matchIfMissing = true)
@Slf4j
public class InMemoryOnlinePresenceServiceImpl implements OnlinePresenceService {

    // userId -> expiry epoch millis
    private final Map<Long, Long> onlineUsers = new ConcurrentHashMap<>();
    // userId -> last seen ISO string
    private final Map<Long, String> lastSeen = new ConcurrentHashMap<>();
    // chatRoomId -> set of typing userIds with expiry
    private final Map<Long, Map<Long, Long>> typingUsers = new ConcurrentHashMap<>();

    private static final long HEARTBEAT_MS = 60_000L;  // 1 minute
    private static final long TYPING_MS = 10_000L;     // 10 seconds

    @Override
    public void markUserOnline(Long userId) {
        log.debug("(in-memory) Marking user {} as online", userId);
        onlineUsers.put(userId, System.currentTimeMillis() + HEARTBEAT_MS);
        lastSeen.put(userId, Instant.now().toString());
    }

    @Override
    public void markUserOffline(Long userId) {
        log.debug("(in-memory) Marking user {} as offline", userId);
        onlineUsers.remove(userId);
        lastSeen.put(userId, Instant.now().toString());
    }

    @Override
    public boolean isUserOnline(Long userId) {
        Long expiry = onlineUsers.get(userId);
        if (expiry == null) return false;
        if (System.currentTimeMillis() > expiry) {
            onlineUsers.remove(userId);
            return false;
        }
        return true;
    }

    @Override
    public Set<Long> getOnlineUsers() {
        long now = System.currentTimeMillis();
        onlineUsers.entrySet().removeIf(e -> now > e.getValue());
        return Collections.unmodifiableSet(onlineUsers.keySet());
    }

    @Override
    public String getLastSeen(Long userId) {
        return lastSeen.get(userId);
    }

    @Override
    public void setTypingStatus(Long userId, Long chatRoomId, boolean isTyping) {
        Map<Long, Long> roomTypers = typingUsers.computeIfAbsent(chatRoomId, k -> new ConcurrentHashMap<>());
        if (isTyping) {
            log.debug("(in-memory) User {} is typing in room {}", userId, chatRoomId);
            roomTypers.put(userId, System.currentTimeMillis() + TYPING_MS);
        } else {
            log.debug("(in-memory) User {} stopped typing in room {}", userId, chatRoomId);
            roomTypers.remove(userId);
        }
    }

    @Override
    public Set<Long> getTypingUsers(Long chatRoomId) {
        Map<Long, Long> roomTypers = typingUsers.get(chatRoomId);
        if (roomTypers == null) return Set.of();
        long now = System.currentTimeMillis();
        roomTypers.entrySet().removeIf(e -> now > e.getValue());
        return Collections.unmodifiableSet(roomTypers.keySet());
    }
}
