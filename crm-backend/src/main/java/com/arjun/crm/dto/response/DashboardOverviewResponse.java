package com.arjun.crm.dto.response;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardOverviewResponse implements Serializable {
    
    private TaskStatistics taskStatistics;
    private ProjectStatistics projectStatistics;
    private NotificationStatistics notificationStatistics;
    private ActivityStatistics activityStatistics;
    private UserProductivity userProductivity;
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TaskStatistics implements Serializable {
        private Long totalTasks;
        private Long completedTasks;
        private Long overdueTasks;
        private Long inProgressTasks;
        private Double completionRate;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProjectStatistics implements Serializable {
        private Long totalProjects;
        private Long activeProjects;
        private Long completedProjects;
        private Double averageProgress;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class NotificationStatistics implements Serializable {
        private Long unreadCount;
        private Long totalCount;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ActivityStatistics implements Serializable {
        private Long todayActivities;
        private Long weekActivities;
        private Long monthActivities;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserProductivity implements Serializable {
        private Long tasksCompleted;
        private Long commentsPosted;
        private Long messagesSet;
        private Double activityScore;
    }
}
