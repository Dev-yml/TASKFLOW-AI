package com.arjun.crm.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TypingIndicatorRequest {

    @NotNull(message = "Chat room ID is required")
    private Long chatRoomId;

    @NotNull(message = "Typing status is required")
    private Boolean isTyping;
}
