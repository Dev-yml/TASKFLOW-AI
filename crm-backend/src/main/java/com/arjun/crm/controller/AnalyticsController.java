package com.arjun.crm.controller;

import com.arjun.crm.dto.response.ActivityAnalyticsResponse;
import com.arjun.crm.dto.response.ApiResponse;
import com.arjun.crm.dto.response.TaskAnalyticsResponse;
import com.arjun.crm.dto.response.TeamPerformanceResponse;
import com.arjun.crm.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@Slf4j
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    /**
     * Get task analytics
     * GET /api/analytics/tasks
     */
    @GetMapping("/tasks")
    public ResponseEntity<ApiResponse<TaskAnalyticsResponse>> getTaskAnalytics(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        if (startDate == null) {
            startDate = LocalDate.now().minusDays(30);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }
        
        try {
            TaskAnalyticsResponse response = analyticsService.getTaskAnalytics(startDate, endDate);
            return ResponseEntity.ok(ApiResponse.success("Task analytics retrieved successfully", response));
        } catch (Exception e) {
            log.error("Failed to retrieve task analytics: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve task analytics: " + e.getMessage()));
        }
    }

    /**
     * Get team performance analytics
     * GET /api/analytics/team
     */
    @GetMapping("/team")
    public ResponseEntity<ApiResponse<TeamPerformanceResponse>> getTeamPerformance(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        if (startDate == null) {
            startDate = LocalDate.now().minusDays(30);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }
        
        try {
            TeamPerformanceResponse response = analyticsService.getTeamPerformance(startDate, endDate);
            return ResponseEntity.ok(ApiResponse.success("Team performance retrieved successfully", response));
        } catch (Exception e) {
            log.error("Failed to retrieve team performance: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve team performance: " + e.getMessage()));
        }
    }

    /**
     * Get activity analytics
     * GET /api/analytics/activity
     */
    @GetMapping("/activity")
    public ResponseEntity<ApiResponse<ActivityAnalyticsResponse>> getActivityAnalytics(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        if (startDate == null) {
            startDate = LocalDate.now().minusDays(30);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }
        
        try {
            ActivityAnalyticsResponse response = analyticsService.getActivityAnalytics(startDate, endDate);
            return ResponseEntity.ok(ApiResponse.success("Activity analytics retrieved successfully", response));
        } catch (Exception e) {
            log.error("Failed to retrieve activity analytics: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve activity analytics: " + e.getMessage()));
        }
    }
}
