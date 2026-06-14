package com.arjun.crm.service;

import com.arjun.crm.dto.request.ChatRoomCreateRequest;
import com.arjun.crm.dto.response.ChatRoomResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Set;

public interface ChatRoomService {
    ChatRoomResponse createChatRoom(ChatRoomCreateRequest request);
    ChatRoomResponse getChatRoom(Long roomId);
    Page<ChatRoomResponse> getUserChatRooms(Pageable pageable);
    ChatRoomResponse getOrCreatePrivateChat(Long otherUserId);
    void addParticipant(Long roomId, Long userId);
    void removeParticipant(Long roomId, Long userId);
    void updateLastRead(Long roomId);
    Long getUnreadCount(Long roomId);

    /** Delete a chat room and all its messages (owner only) */
    void deleteRoom(Long roomId);

    /** Block a user — they can no longer message the current user */
    void blockUser(Long blockedUserId);

    /** Unblock a user */
    void unblockUser(Long blockedUserId);

    /** Get IDs of users the current user has blocked */
    Set<Long> getBlockedUsers();
}
