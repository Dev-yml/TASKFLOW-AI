package com.arjun.crm.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeeklySummaryResponse {
    private Integer tasksCompleted;
    private Integer newLeadsAdded;
    private Integer activeProjects;
    private Integer overdueTasks;
    private String topContributor;
    private String highestRiskProject;
    private String summaryText;
}
