package com.arjun.crm.ai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response DTO for AI smart reply suggestions
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmartReplyResponse {
    private List<String> suggestions;
    private List<String> followUpQuestions;
    private List<String> actionRecommendations;
}
