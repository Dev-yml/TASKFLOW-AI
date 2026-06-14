package com.arjun.crm.service;

import com.arjun.crm.dto.response.WorkspaceInsightsResponse;

public interface AIInsightService {
    
    /**
     * Retrieve all AI insights for the specified workspace.
     * 
     * @param workspaceId ID of the workspace
     * @param forceRefresh if true, bypass Redis cache and generate new insights
     * @return consolidated insights response
     */
    WorkspaceInsightsResponse getWorkspaceInsights(Long workspaceId, boolean forceRefresh);
}
