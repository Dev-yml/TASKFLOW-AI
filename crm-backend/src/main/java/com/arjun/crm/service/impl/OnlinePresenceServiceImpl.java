package com.arjun.crm.service.impl;

import com.arjun.crm.service.OnlinePresenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OnlinePresenceServiceImpl implements OnlinePresenceService {

    private final RedisTemplate<String, String> redisTemplate;

    // Each user gets their own key — no shared TTL that wipes everyone at once
    private static final String ONLINE_USER_KEY_PREFIX = "chat:online:user:";
    // A separate set tracks who is online (no TTL — managed manually)
    private static final String ONLINE_USERS_SET_KEY = "chat:online:users";
    // Last-seen timestamp per user
    private static final String LAST_SEEN_KEY_PREFIX = "chat:lastseen:user:";

    private static final String TYPING_KEY_PREFIX = "chat:typing:room:";
    private static final long ONLINE_HEARTBEAT_SECONDS = 60; // 1 min heartbeat TTL per user
    private static final long TYPING_TIMEOUT_SECONDS = 10;

    @Override
    public void markUserOnline(Long userId) {
        log.debug("Marking user {} as online", userId);
        String userKey = ONLINE_USER_KEY_PREFIX + userId;
        // Per-user key with rolling TTL — refreshed on every heartbeat/connect
        redisTemplate.opsForValue().set(userKey, "1", ONLINE_HEARTBEAT_SECONDS, TimeUnit.SECONDS);
        // Add to the global set (no TTL on the set itself)
        redisTemplate.opsForSet().add(ONLINE_USERS_SET_KEY, userId.toString());
        // Record last-seen (no TTL — kept forever for "last seen X ago")
        redisTemplate.opsForValue().set(LAST_SEEN_KEY_PREFIX + userId, Instant.now().toString());
    }

    @Override
    public void markUserOffline(Long userId) {
        log.debug("Marking user {} as offline", userId);
        redisTemplate.delete(ONLINE_USER_KEY_PREFIX + userId);
        redisTemplate.opsForSet().remove(ONLINE_USERS_SET_KEY, userId.toString());
        // Keep last-seen timestamp when going offline
        redisTemplate.opsForValue().set(LAST_SEEN_KEY_PREFIX + userId, Instant.now().toString());
    }

    @Override
    public boolean isUserOnline(Long userId) {
        // A user is online only if their per-user heartbeat key still exists
        Boolean exists = redisTemplate.hasKey(ONLINE_USER_KEY_PREFIX + userId);
        if (!Boolean.TRUE.equals(exists)) {
            // Clean up the set if the heartbeat key expired
            redisTemplate.opsForSet().remove(ONLINE_USERS_SET_KEY, userId.toString());
            return false;
        }
        return true;
    }

    @Override
    public Set<Long> getOnlineUsers() {
        Set<String> members = redisTemplate.opsForSet().members(ONLINE_USERS_SET_KEY);
        if (members == null) return Set.of();

        // Filter out any users whose heartbeat key has expired (stale set entries)
        return members.stream()
                .map(Long::parseLong)
                .filter(this::isUserOnline)
                .collect(Collectors.toSet());
    }

    @Override
    public String getLastSeen(Long userId) {
        return redisTemplate.opsForValue().get(LAST_SEEN_KEY_PREFIX + userId);
    }

    @Override
    public void setTypingStatus(Long userId, Long chatRoomId, boolean isTyping) {
        String key = TYPING_KEY_PREFIX + chatRoomId;
        if (isTyping) {
            log.debug("User {} is typing in room {}", userId, chatRoomId);
            redisTemplate.opsForSet().add(key, userId.toString());
            redisTemplate.expire(key, TYPING_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } else {
            log.debug("User {} stopped typing in room {}", userId, chatRoomId);
            redisTemplate.opsForSet().remove(key, userId.toString());
        }
    }

    @Override
    public Set<Long> getTypingUsers(Long chatRoomId) {
        String key = TYPING_KEY_PREFIX + chatRoomId;
        Set<String> members = redisTemplate.opsForSet().members(key);
        if (members == null) return Set.of();
        return members.stream().map(Long::parseLong).collect(Collectors.toSet());
    }
}
