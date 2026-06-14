package com.arjun.crm.ai.controller;

import com.arjun.crm.ai.dto.request.ChatSummarizationRequest;
import com.arjun.crm.ai.dto.request.SmartReplyRequest;
import com.arjun.crm.ai.dto.response.ChatSummarizationResponse;
import com.arjun.crm.ai.dto.response.SmartReplyResponse;
import com.arjun.crm.ai.service.AIChatService;
import com.arjun.crm.dto.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

/**
 * Controller for AI-powered chat features
 */
@RestController
@RequestMapping("/api/ai/chat")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@Slf4j
public class AIChatController {

    private final AIChatService aiChatService;

    /**
     * Summarize chat conversation
     * POST /api/ai/chat/summarize
     */
    @PostMapping("/summarize")
    public ResponseEntity<ApiResponse<ChatSummarizationResponse>> summarizeChat(
            @Valid @RequestBody ChatSummarizationRequest request) {
        try {
            ChatSummarizationResponse response = aiChatService.summarizeChat(request);
            return ResponseEntity.ok(ApiResponse.success(
                    "Chat summarization completed successfully", response));
        } catch (Exception e) {
            log.error("AI chat summarization failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(ApiResponse.error("AI service is temporarily unavailable. Please try again later."));
        }
    }

    /**
     * Generate smart reply suggestions
     * POST /api/ai/chat/reply-suggestions
     */
    @PostMapping("/reply-suggestions")
    public ResponseEntity<ApiResponse<SmartReplyResponse>> generateSmartReplies(
            @Valid @RequestBody SmartReplyRequest request) {
        try {
            SmartReplyResponse response = aiChatService.generateSmartReplies(request);
            return ResponseEntity.ok(ApiResponse.success(
                    "Smart reply suggestions generated successfully", response));
        } catch (Exception e) {
            log.error("AI smart reply generation failed: {}", e.getMessage(), e);
            SmartReplyResponse fallback = SmartReplyResponse.builder()
                    .suggestions(Collections.emptyList())
                    .build();
            return ResponseEntity.ok(ApiResponse.success(
                    "AI service is temporarily unavailable. No suggestions generated.", fallback));
        }
    }
}
