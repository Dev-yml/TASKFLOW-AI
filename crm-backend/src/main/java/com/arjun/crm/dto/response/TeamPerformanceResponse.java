package com.arjun.crm.dto.response;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamPerformanceResponse implements Serializable {
    
    private List<UserPerformance> topPerformers;
    private Double averageTasksPerUser;
    private Double averageResponseTime;
    private Long totalTeamTasks;
    private Long totalTeamMessages;
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserPerformance implements Serializable {
        private Long userId;
        private String userName;
        private String userEmail;
        private Long tasksCompleted;
        private Long commentsPosted;
        private Long messagesSet;
        private Double activityScore;
        private Integer rank;
    }
}
