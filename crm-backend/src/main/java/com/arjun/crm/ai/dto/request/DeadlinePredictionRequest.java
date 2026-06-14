package com.arjun.crm.ai.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for AI deadline prediction
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeadlinePredictionRequest {
    
    @NotNull(message = "Task ID is required")
    private Long taskId;
    
    private String taskTitle;
    private String taskDescription;
    private String taskComplexity;
    private Double averageCompletionDays;
    private Double userProductivityScore;
    private Integer currentWorkload;
}
