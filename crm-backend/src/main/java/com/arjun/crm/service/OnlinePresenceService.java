package com.arjun.crm.service;

import java.util.Set;

public interface OnlinePresenceService {
    void markUserOnline(Long userId);
    void markUserOffline(Long userId);
    boolean isUserOnline(Long userId);
    Set<Long> getOnlineUsers();
    String getLastSeen(Long userId);
    void setTypingStatus(Long userId, Long chatRoomId, boolean isTyping);
    Set<Long> getTypingUsers(Long chatRoomId);
}
