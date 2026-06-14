package com.arjun.crm.controller;

import com.arjun.crm.dto.response.ApiResponse;
import com.arjun.crm.dto.response.WorkspaceInsightsResponse;
import com.arjun.crm.service.AIInsightService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai-insights")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@Slf4j
public class AIInsightController {

    private final AIInsightService aiInsightService;

    /**
     * Get workspace insights dashboard data
     * GET /api/ai-insights/dashboard/{workspaceId}
     */
    @GetMapping("/dashboard/{workspaceId}")
    public ResponseEntity<ApiResponse<WorkspaceInsightsResponse>> getDashboardInsights(
            @PathVariable Long workspaceId,
            @RequestParam(defaultValue = "false") boolean refresh) {
        
        log.info("Request received for workspace insights dashboard. Workspace: {}, refresh={}", workspaceId, refresh);
        
        try {
            WorkspaceInsightsResponse response = aiInsightService.getWorkspaceInsights(workspaceId, refresh);
            return ResponseEntity.ok(ApiResponse.success("Workspace insights retrieved successfully", response));
        } catch (Exception e) {
            log.error("Failed to retrieve workspace insights: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve workspace insights: " + e.getMessage()));
        }
    }
}
