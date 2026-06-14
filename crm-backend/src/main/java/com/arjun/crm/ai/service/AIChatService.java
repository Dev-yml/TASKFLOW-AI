package com.arjun.crm.ai.service;

import com.arjun.crm.ai.dto.request.ChatSummarizationRequest;
import com.arjun.crm.ai.dto.request.SmartReplyRequest;
import com.arjun.crm.ai.dto.response.ChatSummarizationResponse;
import com.arjun.crm.ai.dto.response.SmartReplyResponse;

/**
 * Service for AI-powered chat features
 */
public interface AIChatService {
    
    /**
     * Summarize chat conversation
     */
    ChatSummarizationResponse summarizeChat(ChatSummarizationRequest request);
    
    /**
     * Generate smart reply suggestions
     */
    SmartReplyResponse generateSmartReplies(SmartReplyRequest request);
}
