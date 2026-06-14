package com.arjun.crm.ai.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Request DTO for AI task prioritization
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskPrioritizationRequest {
    
    @NotNull(message = "Task ID is required")
    private Long taskId;
    
    private String taskTitle;
    private String taskDescription;
    private LocalDate dueDate;
    private String currentPriority;
    private Integer userWorkload;
    private Integer overdueTaskCount;
    private Double teamProductivity;
}
