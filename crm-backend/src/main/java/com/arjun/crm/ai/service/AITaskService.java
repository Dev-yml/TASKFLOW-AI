package com.arjun.crm.ai.service;

import com.arjun.crm.ai.dto.request.DeadlinePredictionRequest;
import com.arjun.crm.ai.dto.request.TaskPrioritizationRequest;
import com.arjun.crm.ai.dto.response.DeadlinePredictionResponse;
import com.arjun.crm.ai.dto.response.TaskPrioritizationResponse;

/**
 * Service for AI-powered task features
 */
public interface AITaskService {
    
    /**
     * Get AI-powered task prioritization suggestion
     */
    TaskPrioritizationResponse prioritizeTask(TaskPrioritizationRequest request);
    
    /**
     * Get AI-powered deadline prediction
     */
    DeadlinePredictionResponse predictDeadline(DeadlinePredictionRequest request);
}
