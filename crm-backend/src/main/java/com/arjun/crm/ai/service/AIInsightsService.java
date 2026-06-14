package com.arjun.crm.ai.service;

import com.arjun.crm.ai.dto.response.ProductivityInsightsResponse;

/**
 * AI productivity insights for the current authenticated user.
 * Workspace-level insights are handled by AIInsightService (service package).
 */
public interface AIInsightsService {

    ProductivityInsightsResponse getProductivityInsights();
}
