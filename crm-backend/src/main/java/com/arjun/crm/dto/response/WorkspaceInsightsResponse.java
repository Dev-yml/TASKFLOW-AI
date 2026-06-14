package com.arjun.crm.dto.response;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkspaceInsightsResponse {
    private WorkspaceHealthResponse health;
    private List<ProjectRiskResponse> projectRisks;
    private List<TeamWorkloadResponse> workload;
    private List<AIRecommendationResponse> recommendations;
    private List<CRMInsightResponse> crmInsights;
    private WeeklySummaryResponse weeklySummary;
    private List<PredictionResponse> predictions;
    private WorkspaceTrendsResponse trends;
}
