package com.arjun.crm.dto.response;

import com.arjun.crm.enums.TaskPriority;
import com.arjun.crm.enums.TaskStatus;
import lombok.*;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskAnalyticsResponse implements Serializable {
    
    private Long totalTasks;
    private Long completedTasks;
    private Long overdueTasks;
    private Long inProgressTasks;
    private Double completionRate;
    private Double averageCompletionDays;
    private Map<TaskStatus, Long> tasksByStatus;
    private Map<TaskPriority, Long> tasksByPriority;
    private Map<String, Long> tasksByProject;
    private Map<String, Long> tasksByUser;
}
