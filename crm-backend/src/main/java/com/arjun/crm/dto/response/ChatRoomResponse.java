package com.arjun.crm.dto.response;

import com.arjun.crm.entity.ChatRoom;
import com.arjun.crm.enums.ChatRoomType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomResponse {
    
    private Long id;
    private String name;
    private ChatRoomType type;
    private Long workspaceId;
    private String workspaceName;
    private Long projectId;
    private String projectName;
    private Long createdById;
    private String createdByName;
    private String createdByEmail;
    private List<ChatParticipantResponse> participants;
    private ChatMessageResponse lastMessage;
    private Long unreadCount;
    private LocalDateTime createdAt;

    public static ChatRoomResponse fromEntity(ChatRoom chatRoom) {
        return ChatRoomResponse.builder()
                .id(chatRoom.getId())
                .name(chatRoom.getName())
                .type(chatRoom.getType())
                .workspaceId(chatRoom.getWorkspace() != null ? chatRoom.getWorkspace().getId() : null)
                .workspaceName(chatRoom.getWorkspace() != null ? chatRoom.getWorkspace().getName() : null)
                .projectId(chatRoom.getProject() != null ? chatRoom.getProject().getId() : null)
                .projectName(chatRoom.getProject() != null ? chatRoom.getProject().getName() : null)
                .createdById(chatRoom.getCreatedBy().getId())
                .createdByName(chatRoom.getCreatedBy().getFullName())
                .createdByEmail(chatRoom.getCreatedBy().getEmail())
                .participants(chatRoom.getParticipants().stream()
                        .map(ChatParticipantResponse::fromEntity)
                        .collect(Collectors.toList()))
                .createdAt(chatRoom.getCreatedAt())
                .build();
    }
}
