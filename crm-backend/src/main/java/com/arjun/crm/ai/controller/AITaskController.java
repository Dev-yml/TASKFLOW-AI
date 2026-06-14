package com.arjun.crm.ai.controller;

import com.arjun.crm.ai.dto.request.DeadlinePredictionRequest;
import com.arjun.crm.ai.dto.request.TaskPrioritizationRequest;
import com.arjun.crm.ai.dto.response.DeadlinePredictionResponse;
import com.arjun.crm.ai.dto.response.TaskPrioritizationResponse;
import com.arjun.crm.ai.service.AITaskService;
import com.arjun.crm.dto.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for AI-powered task features
 */
@RestController
@RequestMapping("/api/ai/tasks")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class AITaskController {

    private final AITaskService aiTaskService;

    /**
     * Get AI-powered task prioritization suggestion
     * POST /api/ai/tasks/prioritize
     */
    @PostMapping("/prioritize")
    public ResponseEntity<ApiResponse<TaskPrioritizationResponse>> prioritizeTask(
            @Valid @RequestBody TaskPrioritizationRequest request) {
        
        TaskPrioritizationResponse response = aiTaskService.prioritizeTask(request);
        return ResponseEntity.ok(ApiResponse.success(
                "AI task prioritization completed successfully", response));
    }

    /**
     * Get AI-powered deadline prediction
     * POST /api/ai/tasks/deadline-predict
     */
    @PostMapping("/deadline-predict")
    public ResponseEntity<ApiResponse<DeadlinePredictionResponse>> predictDeadline(
            @Valid @RequestBody DeadlinePredictionRequest request) {
        
        DeadlinePredictionResponse response = aiTaskService.predictDeadline(request);
        return ResponseEntity.ok(ApiResponse.success(
                "AI deadline prediction completed successfully", response));
    }
}
