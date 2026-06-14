package com.arjun.crm.dto.response;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportResponse implements Serializable {
    
    private String reportType; // DAILY, WEEKLY, MONTHLY
    private LocalDate startDate;
    private LocalDate endDate;
    private TaskSummary taskSummary;
    private ProjectSummary projectSummary;
    private TeamSummary teamSummary;
    private ActivitySummary activitySummary;
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TaskSummary implements Serializable {
        private Long tasksCreated;
        private Long tasksCompleted;
        private Long tasksOverdue;
        private Double completionRate;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProjectSummary implements Serializable {
        private Long projectsCreated;
        private Long projectsCompleted;
        private Double averageProgress;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TeamSummary implements Serializable {
        private Long activeUsers;
        private Long totalMessages;
        private Long totalComments;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ActivitySummary implements Serializable {
        private Long totalActivities;
        private String mostActiveUser;
        private String mostActiveProject;
    }
}
