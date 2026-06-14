package com.arjun.crm.ai.service.impl;

import com.arjun.crm.ai.dto.request.ChatSummarizationRequest;
import com.arjun.crm.ai.dto.request.SmartReplyRequest;
import com.arjun.crm.ai.dto.response.AIResponse;
import com.arjun.crm.ai.dto.response.ChatSummarizationResponse;
import com.arjun.crm.ai.dto.response.SmartReplyResponse;
import com.arjun.crm.ai.parser.AIResponseParser;
import com.arjun.crm.ai.prompt.PromptBuilder;
import com.arjun.crm.ai.provider.XAIProvider;
import com.arjun.crm.ai.service.AIChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * Implementation of AI Chat Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AIChatServiceImpl implements AIChatService {

    private final XAIProvider xaiProvider;
    private final PromptBuilder promptBuilder;
    private final AIResponseParser responseParser;

    @Override
    public ChatSummarizationResponse summarizeChat(ChatSummarizationRequest request) {
        log.info("AI chat summarization requested for chat room ID: {}", request.getChatRoomId());
        
        // Build prompt
        String prompt = promptBuilder.buildChatSummarizationPrompt(request);
        
        // Get AI response (no cache for dynamic content)
        AIResponse aiResponse = xaiProvider.generateResponseNoCache(prompt);
        
        // Parse response
        return responseParser.parseChatSummarization(aiResponse.getContent(), request.getChatRoomId());
    }

    @Override
    public SmartReplyResponse generateSmartReplies(SmartReplyRequest request) {
        log.info("AI smart reply generation requested");
        
        // Build prompt
        String prompt = promptBuilder.buildSmartReplyPrompt(request);
        
        // Get AI response (no cache for dynamic content)
        AIResponse aiResponse = xaiProvider.generateResponseNoCache(prompt);
        
        // Parse response
        return responseParser.parseSmartReply(aiResponse.getContent());
    }

    /**
     * Async version of chat summarization
     */
    @Async
    public CompletableFuture<ChatSummarizationResponse> summarizeChatAsync(ChatSummarizationRequest request) {
        return CompletableFuture.completedFuture(summarizeChat(request));
    }

    /**
     * Async version of smart reply generation
     */
    @Async
    public CompletableFuture<SmartReplyResponse> generateSmartRepliesAsync(SmartReplyRequest request) {
        return CompletableFuture.completedFuture(generateSmartReplies(request));
    }
}
