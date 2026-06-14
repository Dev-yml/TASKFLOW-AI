package com.arjun.crm.ai.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request DTO for AI chat summarization
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatSummarizationRequest {
    
    @NotNull(message = "Chat room ID is required")
    private Long chatRoomId;
    
    private String chatRoomName;
    private List<String> messages;
    private Integer messageCount;
    private String context;
}
