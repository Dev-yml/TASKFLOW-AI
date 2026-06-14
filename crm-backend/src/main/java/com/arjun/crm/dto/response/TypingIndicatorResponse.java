package com.arjun.crm.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TypingIndicatorResponse {
    
    private Long chatRoomId;
    private Long userId;
    private String userName;
    private Boolean isTyping;
}
