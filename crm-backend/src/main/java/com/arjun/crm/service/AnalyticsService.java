package com.arjun.crm.service;

import com.arjun.crm.dto.response.ActivityAnalyticsResponse;
import com.arjun.crm.dto.response.TaskAnalyticsResponse;
import com.arjun.crm.dto.response.TeamPerformanceResponse;

import java.time.LocalDate;

public interface AnalyticsService {
    
    /**
     * Get task analytics
     */
    TaskAnalyticsResponse getTaskAnalytics(LocalDate startDate, LocalDate endDate);
    
    /**
     * Get team performance analytics
     */
    TeamPerformanceResponse getTeamPerformance(LocalDate startDate, LocalDate endDate);
    
    /**
     * Get activity analytics
     */
    ActivityAnalyticsResponse getActivityAnalytics(LocalDate startDate, LocalDate endDate);
}
