package com.arjun.crm.dto.response;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkspaceHealthResponse {
    private Integer score;
    private String status; // Healthy, Moderate Risk, Critical
    private String explanation;
    private List<HealthFactor> factors;
    private List<WhyFactor> why;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class HealthFactor {
        private String name; // Overdue Tasks, Stalled Projects, Inactive Leads, Team Workload Balance
        private String value;
        private String status; // SUCCESS, WARNING, DANGER
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WhyFactor {
        private String type; // SUCCESS, WARNING, DANGER
        private String text; // e.g. "3 overdue tasks"
    }
}
