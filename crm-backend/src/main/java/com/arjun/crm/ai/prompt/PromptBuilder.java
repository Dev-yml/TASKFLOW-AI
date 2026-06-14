package com.arjun.crm.ai.prompt;

import com.arjun.crm.ai.dto.request.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Builder for AI prompts
 */
@Component
public class PromptBuilder {

    /**
     * Build prompt for task prioritization
     */
    public String buildTaskPrioritizationPrompt(TaskPrioritizationRequest request) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are an AI assistant for task management. Analyze the following task and suggest the optimal priority level.\n\n");
        
        prompt.append("Task Details:\n");
        prompt.append("- Title: ").append(request.getTaskTitle()).append("\n");
        prompt.append("- Description: ").append(request.getTaskDescription()).append("\n");
        prompt.append("- Current Priority: ").append(request.getCurrentPriority()).append("\n");
        
        if (request.getDueDate() != null) {
            long daysUntilDue = ChronoUnit.DAYS.between(LocalDate.now(), request.getDueDate());
            prompt.append("- Due Date: ").append(request.getDueDate()).append(" (").append(daysUntilDue).append(" days from now)\n");
        }
        
        prompt.append("- User Workload: ").append(request.getUserWorkload()).append(" active tasks\n");
        prompt.append("- Overdue Tasks: ").append(request.getOverdueTaskCount()).append("\n");
        prompt.append("- Team Productivity: ").append(request.getTeamProductivity()).append("%\n\n");
        
        prompt.append("Based on this information, provide:\n");
        prompt.append("1. Suggested Priority (LOW, MEDIUM, HIGH, or URGENT)\n");
        prompt.append("2. Reasoning (2-3 sentences)\n");
        prompt.append("3. Risk Score (0-100)\n");
        prompt.append("4. Recommendation (1-2 sentences)\n\n");
        prompt.append("Format your response as JSON:\n");
        prompt.append("{\n");
        prompt.append("  \"suggestedPriority\": \"...\",\n");
        prompt.append("  \"reasoning\": \"...\",\n");
        prompt.append("  \"riskScore\": ...,\n");
        prompt.append("  \"recommendation\": \"...\"\n");
        prompt.append("}");
        
        return prompt.toString();
    }

    /**
     * Build prompt for deadline prediction
     */
    public String buildDeadlinePredictionPrompt(DeadlinePredictionRequest request) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are an AI assistant for project management. Predict a realistic deadline for the following task.\n\n");
        
        prompt.append("Task Details:\n");
        prompt.append("- Title: ").append(request.getTaskTitle()).append("\n");
        prompt.append("- Description: ").append(request.getTaskDescription()).append("\n");
        prompt.append("- Complexity: ").append(request.getTaskComplexity()).append("\n");
        prompt.append("- Average Completion Time: ").append(request.getAverageCompletionDays()).append(" days\n");
        prompt.append("- User Productivity Score: ").append(request.getUserProductivityScore()).append("/100\n");
        prompt.append("- Current Workload: ").append(request.getCurrentWorkload()).append(" tasks\n\n");
        
        prompt.append("Based on this information, provide:\n");
        prompt.append("1. Estimated Days (integer)\n");
        prompt.append("2. Confidence Level (0-100)\n");
        prompt.append("3. Risk Assessment (LOW, MEDIUM, or HIGH)\n");
        prompt.append("4. Recommendation (1-2 sentences)\n\n");
        prompt.append("Format your response as JSON:\n");
        prompt.append("{\n");
        prompt.append("  \"estimatedDays\": ...,\n");
        prompt.append("  \"confidenceLevel\": ...,\n");
        prompt.append("  \"riskAssessment\": \"...\",\n");
        prompt.append("  \"recommendation\": \"...\"\n");
        prompt.append("}");
        
        return prompt.toString();
    }

    /**
     * Build prompt for chat summarization
     */
    public String buildChatSummarizationPrompt(ChatSummarizationRequest request) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are an AI assistant for team collaboration. Summarize the following chat conversation.\n\n");
        
        prompt.append("Chat Room: ").append(request.getChatRoomName()).append("\n");
        prompt.append("Message Count: ").append(request.getMessageCount()).append("\n");
        if (request.getContext() != null) {
            prompt.append("Context: ").append(request.getContext()).append("\n");
        }
        prompt.append("\nMessages:\n");
        
        List<String> messages = request.getMessages();
        if (messages != null && !messages.isEmpty()) {
            for (int i = 0; i < Math.min(messages.size(), 50); i++) {
                prompt.append("- ").append(messages.get(i)).append("\n");
            }
        }
        
        prompt.append("\nProvide:\n");
        prompt.append("1. Summary (2-3 sentences)\n");
        prompt.append("2. Key Points (3-5 bullet points)\n");
        prompt.append("3. Action Items (if any)\n");
        prompt.append("4. Decisions Made (if any)\n\n");
        prompt.append("Format your response as JSON:\n");
        prompt.append("{\n");
        prompt.append("  \"summary\": \"...\",\n");
        prompt.append("  \"keyPoints\": [\"...\", \"...\"],\n");
        prompt.append("  \"actionItems\": [\"...\", \"...\"],\n");
        prompt.append("  \"decisions\": [\"...\", \"...\"]\n");
        prompt.append("}");
        
        return prompt.toString();
    }

    /**
     * Build prompt for smart reply suggestions
     */
    public String buildSmartReplyPrompt(SmartReplyRequest request) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are an AI assistant for professional communication. Generate smart reply suggestions for the following message.\n\n");
        
        prompt.append("Message: ").append(request.getMessage()).append("\n");
        
        if (request.getContext() != null) {
            prompt.append("Context: ").append(request.getContext()).append("\n");
        }
        
        if (request.getConversationHistory() != null) {
            prompt.append("Conversation History: ").append(request.getConversationHistory()).append("\n");
        }
        
        int count = request.getSuggestionCount() != null ? request.getSuggestionCount() : 3;
        
        prompt.append("\nProvide:\n");
        prompt.append("1. ").append(count).append(" Reply Suggestions (professional, concise)\n");
        prompt.append("2. 2-3 Follow-up Questions (if applicable)\n");
        prompt.append("3. 2-3 Action Recommendations (if applicable)\n\n");
        prompt.append("Format your response as JSON:\n");
        prompt.append("{\n");
        prompt.append("  \"suggestions\": [\"...\", \"...\", \"...\"],\n");
        prompt.append("  \"followUpQuestions\": [\"...\", \"...\"],\n");
        prompt.append("  \"actionRecommendations\": [\"...\", \"...\"]\n");
        prompt.append("}");
        
        return prompt.toString();
    }

    /**
     * Build prompt for productivity insights
     */
    public String buildProductivityInsightsPrompt(Long userId, int tasksCompleted, int tasksOverdue, 
                                                   double completionRate, double activityScore) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are an AI assistant for productivity analysis. Analyze the following user productivity metrics.\n\n");
        
        prompt.append("Productivity Metrics:\n");
        prompt.append("- Tasks Completed: ").append(tasksCompleted).append("\n");
        prompt.append("- Tasks Overdue: ").append(tasksOverdue).append("\n");
        prompt.append("- Completion Rate: ").append(completionRate).append("%\n");
        prompt.append("- Activity Score: ").append(activityScore).append("\n\n");
        
        prompt.append("Provide:\n");
        prompt.append("1. Productivity Score (0-100)\n");
        prompt.append("2. Overall Assessment (2-3 sentences)\n");
        prompt.append("3. Strengths (2-3 points)\n");
        prompt.append("4. Areas for Improvement (2-3 points)\n");
        prompt.append("5. Recommendations (2-3 actionable suggestions)\n");
        prompt.append("6. Trend (IMPROVING, STABLE, or DECLINING)\n\n");
        prompt.append("Format your response as JSON:\n");
        prompt.append("{\n");
        prompt.append("  \"productivityScore\": ...,\n");
        prompt.append("  \"overallAssessment\": \"...\",\n");
        prompt.append("  \"strengths\": [\"...\", \"...\"],\n");
        prompt.append("  \"improvements\": [\"...\", \"...\"],\n");
        prompt.append("  \"recommendations\": [\"...\", \"...\"],\n");
        prompt.append("  \"trend\": \"...\"\n");
        prompt.append("}");
        
        return prompt.toString();
    }
}
