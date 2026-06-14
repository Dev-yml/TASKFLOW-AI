package com.arjun.crm.dto.request;

import com.arjun.crm.enums.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageRequest {

    @NotNull(message = "Chat room ID is required")
    private Long chatRoomId;

    @NotBlank(message = "Message content is required")
    @Size(max = 5000, message = "Message cannot exceed 5000 characters")
    private String content;

    private MessageType messageType = MessageType.TEXT;
}
