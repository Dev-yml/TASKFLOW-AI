package com.arjun.crm.ai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for AI task prioritization
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskPrioritizationResponse {
    private Long taskId;
    private String suggestedPriority;
    private String reasoning;
    private Double riskScore;
    private String recommendation;
}
