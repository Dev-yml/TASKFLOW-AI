package com.arjun.crm.ai.service.impl;

import com.arjun.crm.ai.dto.request.DeadlinePredictionRequest;
import com.arjun.crm.ai.dto.request.TaskPrioritizationRequest;
import com.arjun.crm.ai.dto.response.AIResponse;
import com.arjun.crm.ai.dto.response.DeadlinePredictionResponse;
import com.arjun.crm.ai.dto.response.TaskPrioritizationResponse;
import com.arjun.crm.ai.parser.AIResponseParser;
import com.arjun.crm.ai.prompt.PromptBuilder;
import com.arjun.crm.ai.provider.XAIProvider;
import com.arjun.crm.ai.service.AITaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * Implementation of AI Task Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AITaskServiceImpl implements AITaskService {

    private final XAIProvider xaiProvider;
    private final PromptBuilder promptBuilder;
    private final AIResponseParser responseParser;

    @Override
    public TaskPrioritizationResponse prioritizeTask(TaskPrioritizationRequest request) {
        log.info("AI task prioritization requested for task ID: {}", request.getTaskId());
        
        // Build prompt
        String prompt = promptBuilder.buildTaskPrioritizationPrompt(request);
        
        // Get AI response
        AIResponse aiResponse = xaiProvider.generateResponse(prompt);
        
        // Parse response
        return responseParser.parseTaskPrioritization(aiResponse.getContent(), request.getTaskId());
    }

    @Override
    public DeadlinePredictionResponse predictDeadline(DeadlinePredictionRequest request) {
        log.info("AI deadline prediction requested for task ID: {}", request.getTaskId());
        
        // Build prompt
        String prompt = promptBuilder.buildDeadlinePredictionPrompt(request);
        
        // Get AI response
        AIResponse aiResponse = xaiProvider.generateResponse(prompt);
        
        // Parse response
        return responseParser.parseDeadlinePrediction(aiResponse.getContent(), request.getTaskId());
    }

    /**
     * Async version of task prioritization
     */
    @Async
    public CompletableFuture<TaskPrioritizationResponse> prioritizeTaskAsync(TaskPrioritizationRequest request) {
        return CompletableFuture.completedFuture(prioritizeTask(request));
    }

    /**
     * Async version of deadline prediction
     */
    @Async
    public CompletableFuture<DeadlinePredictionResponse> predictDeadlineAsync(DeadlinePredictionRequest request) {
        return CompletableFuture.completedFuture(predictDeadline(request));
    }
}
