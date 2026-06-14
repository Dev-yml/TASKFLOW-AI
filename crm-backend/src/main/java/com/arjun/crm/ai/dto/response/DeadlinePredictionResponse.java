package com.arjun.crm.ai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Response DTO for AI deadline prediction
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeadlinePredictionResponse {
    private Long taskId;
    private LocalDate suggestedDeadline;
    private Integer estimatedDays;
    private Double confidenceLevel;
    private String riskAssessment;
    private String recommendation;
}
