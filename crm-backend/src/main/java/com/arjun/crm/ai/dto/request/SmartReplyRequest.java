package com.arjun.crm.ai.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for AI smart reply suggestions
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmartReplyRequest {
    
    @NotBlank(message = "Message is required")
    private String message;
    
    private String context;
    private String conversationHistory;
    private Integer suggestionCount;
}
