package com.arjun.crm.dto.response;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityAnalyticsResponse implements Serializable {
    
    private Long totalActivities;
    private Map<String, Long> activitiesByType;
    private Map<LocalDate, Long> activitiesByDate;
    private List<MostActiveUser> mostActiveUsers;
    private List<MostActiveProject> mostActiveProjects;
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MostActiveUser implements Serializable {
        private Long userId;
        private String userName;
        private Long activityCount;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MostActiveProject implements Serializable {
        private Long projectId;
        private String projectName;
        private Long activityCount;
    }
}
