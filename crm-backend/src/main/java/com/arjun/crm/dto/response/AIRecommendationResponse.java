package com.arjun.crm.dto.response;

import lombok.*;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AIRecommendationResponse {
    private String id;
    private String type; // WORKLOAD, CRM, TASK_BLOCKER, SCHEDULE
    private String suggestion;
    private String priority; // HIGH, MEDIUM, LOW
    private Boolean actionable;
    private String actionText; // e.g. "Reassign", "Create Follow-up"
    private String actionType; // e.g. "REASSIGN", "FOLLOW_UP", "VIEW_TASK"
    private Map<String, Object> actionPayload; // e.g. { "taskId": 12, "assigneeId": 3 }
}
