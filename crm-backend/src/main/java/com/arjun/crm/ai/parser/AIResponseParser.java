package com.arjun.crm.ai.parser;

import com.arjun.crm.ai.dto.response.*;
import com.arjun.crm.exception.AIServiceException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for AI responses
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AIResponseParser {

    private final ObjectMapper objectMapper;

    /**
     * Parse task prioritization response
     */
    public TaskPrioritizationResponse parseTaskPrioritization(String content, Long taskId) {
        try {
            String jsonContent = extractJson(content);
            JsonNode root = objectMapper.readTree(jsonContent);
            
            return TaskPrioritizationResponse.builder()
                    .taskId(taskId)
                    .suggestedPriority(root.path("suggestedPriority").asText())
                    .reasoning(root.path("reasoning").asText())
                    .riskScore(root.path("riskScore").asDouble())
                    .recommendation(root.path("recommendation").asText())
                    .build();
                    
        } catch (Exception e) {
            log.error("Error parsing task prioritization response: {}", e.getMessage());
            throw new AIServiceException("Failed to parse AI response");
        }
    }

    /**
     * Parse deadline prediction response
     */
    public DeadlinePredictionResponse parseDeadlinePrediction(String content, Long taskId) {
        try {
            String jsonContent = extractJson(content);
            JsonNode root = objectMapper.readTree(jsonContent);
            
            int estimatedDays = root.path("estimatedDays").asInt();
            LocalDate suggestedDeadline = LocalDate.now().plusDays(estimatedDays);
            
            return DeadlinePredictionResponse.builder()
                    .taskId(taskId)
                    .suggestedDeadline(suggestedDeadline)
                    .estimatedDays(estimatedDays)
                    .confidenceLevel(root.path("confidenceLevel").asDouble())
                    .riskAssessment(root.path("riskAssessment").asText())
                    .recommendation(root.path("recommendation").asText())
                    .build();
                    
        } catch (Exception e) {
            log.error("Error parsing deadline prediction response: {}", e.getMessage());
            throw new AIServiceException("Failed to parse AI response");
        }
    }

    /**
     * Parse chat summarization response
     */
    public ChatSummarizationResponse parseChatSummarization(String content, Long chatRoomId) {
        try {
            String jsonContent = extractJson(content);
            JsonNode root = objectMapper.readTree(jsonContent);
            
            return ChatSummarizationResponse.builder()
                    .chatRoomId(chatRoomId)
                    .summary(root.path("summary").asText())
                    .keyPoints(parseStringArray(root.path("keyPoints")))
                    .actionItems(parseStringArray(root.path("actionItems")))
                    .decisions(parseStringArray(root.path("decisions")))
                    .build();
                    
        } catch (Exception e) {
            log.error("Error parsing chat summarization response: {}", e.getMessage());
            throw new AIServiceException("Failed to parse AI response");
        }
    }

    /**
     * Parse smart reply response
     */
    public SmartReplyResponse parseSmartReply(String content) {
        try {
            String jsonContent = extractJson(content);
            JsonNode root = objectMapper.readTree(jsonContent);
            
            return SmartReplyResponse.builder()
                    .suggestions(parseStringArray(root.path("suggestions")))
                    .followUpQuestions(parseStringArray(root.path("followUpQuestions")))
                    .actionRecommendations(parseStringArray(root.path("actionRecommendations")))
                    .build();
                    
        } catch (Exception e) {
            log.error("Error parsing smart reply response: {}", e.getMessage());
            throw new AIServiceException("Failed to parse AI response");
        }
    }

    /**
     * Parse productivity insights response
     */
    public ProductivityInsightsResponse parseProductivityInsights(String content) {
        try {
            String jsonContent = extractJson(content);
            JsonNode root = objectMapper.readTree(jsonContent);
            
            return ProductivityInsightsResponse.builder()
                    .productivityScore(root.path("productivityScore").asDouble())
                    .overallAssessment(root.path("overallAssessment").asText())
                    .strengths(parseStringArray(root.path("strengths")))
                    .improvements(parseStringArray(root.path("improvements")))
                    .recommendations(parseStringArray(root.path("recommendations")))
                    .trend(root.path("trend").asText())
                    .build();
                    
        } catch (Exception e) {
            log.error("Error parsing productivity insights response: {}", e.getMessage());
            throw new AIServiceException("Failed to parse AI response");
        }
    }

    /**
     * Extract JSON from AI response.
     * Handles:
     * - Bare JSON objects/arrays
     * - ```json ... ``` code fences
     * - ``` ... ``` code fences
     * - Prose before/after the JSON block
     */
    private String extractJson(String content) {
        if (content == null || content.isBlank()) {
            throw new AIServiceException("Empty AI response");
        }

        content = content.trim();

        // 1. Try to find a ```json ... ``` fence anywhere in the response
        int fenceStart = content.indexOf("```json");
        if (fenceStart >= 0) {
            int jsonStart = content.indexOf('\n', fenceStart) + 1;
            int fenceEnd = content.indexOf("```", jsonStart);
            if (fenceEnd > jsonStart) {
                return content.substring(jsonStart, fenceEnd).trim();
            }
        }

        // 2. Try to find a plain ``` ... ``` fence
        fenceStart = content.indexOf("```");
        if (fenceStart >= 0) {
            int jsonStart = content.indexOf('\n', fenceStart) + 1;
            int fenceEnd = content.indexOf("```", jsonStart);
            if (fenceEnd > jsonStart) {
                return content.substring(jsonStart, fenceEnd).trim();
            }
        }

        // 3. Try to extract the first { ... } block (handles prose + JSON)
        int braceStart = content.indexOf('{');
        int braceEnd   = content.lastIndexOf('}');
        if (braceStart >= 0 && braceEnd > braceStart) {
            return content.substring(braceStart, braceEnd + 1).trim();
        }

        // 4. Try to extract the first [ ... ] block
        int bracketStart = content.indexOf('[');
        int bracketEnd   = content.lastIndexOf(']');
        if (bracketStart >= 0 && bracketEnd > bracketStart) {
            return content.substring(bracketStart, bracketEnd + 1).trim();
        }

        // 5. Return as-is and let the caller fail with a useful message
        return content;
    }

    /**
     * Parse JSON array to List<String>
     */
    private List<String> parseStringArray(JsonNode arrayNode) {
        List<String> result = new ArrayList<>();
        
        if (arrayNode.isArray()) {
            arrayNode.forEach(node -> result.add(node.asText()));
        }
        
        return result;
    }
}
