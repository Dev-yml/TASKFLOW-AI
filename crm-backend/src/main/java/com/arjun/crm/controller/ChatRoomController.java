package com.arjun.crm.controller;

import com.arjun.crm.dto.request.ChatRoomCreateRequest;
import com.arjun.crm.dto.response.ApiResponse;
import com.arjun.crm.dto.response.ChatRoomResponse;
import com.arjun.crm.service.ChatRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat/rooms")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    /**
     * Create a new chat room
     * POST /api/chat/rooms
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ChatRoomResponse>> createChatRoom(
            @Valid @RequestBody ChatRoomCreateRequest request) {
        
        ChatRoomResponse response = chatRoomService.createChatRoom(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Chat room created successfully", response));
    }

    /**
     * Get user's chat rooms
     * GET /api/chat/rooms
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ChatRoomResponse>>> getUserChatRooms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<ChatRoomResponse> chatRooms = chatRoomService.getUserChatRooms(pageable);
        return ResponseEntity.ok(ApiResponse.success("Chat rooms retrieved successfully", chatRooms));
    }

    /**
     * Get chat room by ID
     * GET /api/chat/rooms/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ChatRoomResponse>> getChatRoom(@PathVariable Long id) {
        ChatRoomResponse response = chatRoomService.getChatRoom(id);
        return ResponseEntity.ok(ApiResponse.success("Chat room retrieved successfully", response));
    }

    /**
     * Get or create private chat
     * POST /api/chat/rooms/private/{userId}
     */
    @PostMapping("/private/{userId}")
    public ResponseEntity<ApiResponse<ChatRoomResponse>> getOrCreatePrivateChat(@PathVariable Long userId) {
        ChatRoomResponse response = chatRoomService.getOrCreatePrivateChat(userId);
        return ResponseEntity.ok(ApiResponse.success("Private chat retrieved successfully", response));
    }

    /**
     * Add participant to chat room
     * POST /api/chat/rooms/{id}/participants/{userId}
     */
    @PostMapping("/{id}/participants/{userId}")
    public ResponseEntity<ApiResponse<Void>> addParticipant(
            @PathVariable Long id,
            @PathVariable Long userId) {
        
        chatRoomService.addParticipant(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Participant added successfully", null));
    }

    /**
     * Remove participant from chat room
     * DELETE /api/chat/rooms/{id}/participants/{userId}
     */
    @DeleteMapping("/{id}/participants/{userId}")
    public ResponseEntity<ApiResponse<Void>> removeParticipant(
            @PathVariable Long id,
            @PathVariable Long userId) {
        
        chatRoomService.removeParticipant(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Participant removed successfully", null));
    }

    /**
     * Mark messages as read
     * PUT /api/chat/rooms/{id}/read
     */
    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(@PathVariable Long id) {
        chatRoomService.updateLastRead(id);
        return ResponseEntity.ok(ApiResponse.success("Messages marked as read", null));
    }

    /**
     * Get unread message count
     * GET /api/chat/rooms/{id}/unread-count
     */
    @GetMapping("/{id}/unread-count")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount(@PathVariable Long id) {
        Long count = chatRoomService.getUnreadCount(id);
        return ResponseEntity.ok(ApiResponse.success("Unread count retrieved successfully", count));
    }

    /**
     * Delete a chat room (creator only — deletes all messages)
     * DELETE /api/chat/rooms/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRoom(@PathVariable Long id) {
        chatRoomService.deleteRoom(id);
        return ResponseEntity.ok(ApiResponse.success("Chat room deleted successfully", null));
    }

    /**
     * Block a user
     * POST /api/chat/block/{userId}
     */
    @PostMapping("/block/{userId}")
    public ResponseEntity<ApiResponse<Void>> blockUser(@PathVariable Long userId) {
        chatRoomService.blockUser(userId);
        return ResponseEntity.ok(ApiResponse.success("User blocked successfully", null));
    }

    /**
     * Unblock a user
     * DELETE /api/chat/block/{userId}
     */
    @DeleteMapping("/block/{userId}")
    public ResponseEntity<ApiResponse<Void>> unblockUser(@PathVariable Long userId) {
        chatRoomService.unblockUser(userId);
        return ResponseEntity.ok(ApiResponse.success("User unblocked successfully", null));
    }

    /**
     * Get list of blocked user IDs for current user
     * GET /api/chat/blocked
     */
    @GetMapping("/blocked")
    public ResponseEntity<ApiResponse<java.util.Set<Long>>> getBlockedUsers() {
        return ResponseEntity.ok(ApiResponse.success("Blocked users retrieved", chatRoomService.getBlockedUsers()));
    }
}
