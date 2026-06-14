package com.arjun.crm.dto.response;

import com.arjun.crm.entity.ChatParticipant;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatParticipantResponse {
    
    private Long id;
    private Long userId;
    private String userName;
    private String userEmail;
    private Boolean isOnline;
    private Instant joinedAt;
    private Instant lastReadAt;

    public static ChatParticipantResponse fromEntity(ChatParticipant participant) {
        return ChatParticipantResponse.builder()
                .id(participant.getId())
                .userId(participant.getUser().getId())
                .userName(participant.getUser().getFullName())
                .userEmail(participant.getUser().getEmail())
                .isOnline(false) // Will be set by service
                .joinedAt(participant.getJoinedAt())
                .lastReadAt(participant.getLastReadAt())
                .build();
    }
}
