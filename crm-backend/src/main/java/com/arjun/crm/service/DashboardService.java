package com.arjun.crm.service;

import com.arjun.crm.dto.response.DashboardOverviewResponse;

public interface DashboardService {
    
    /**
     * Get dashboard overview with all statistics
     */
    DashboardOverviewResponse getDashboardOverview();
}
