package com.arjun.crm.controller;

import com.arjun.crm.dto.response.ApiResponse;
import com.arjun.crm.dto.response.ReportResponse;
import com.arjun.crm.service.ReportingService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class ReportingController {

    private final ReportingService reportingService;

    /**
     * Generate daily report
     * GET /api/reports/daily
     */
    @GetMapping("/daily")
    public ResponseEntity<ApiResponse<ReportResponse>> getDailyReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        if (date == null) {
            date = LocalDate.now().minusDays(1); // Yesterday by default
        }
        
        ReportResponse response = reportingService.generateDailyReport(date);
        return ResponseEntity.ok(ApiResponse.success("Daily report generated successfully", response));
    }

    /**
     * Generate weekly report
     * GET /api/reports/weekly
     */
    @GetMapping("/weekly")
    public ResponseEntity<ApiResponse<ReportResponse>> getWeeklyReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        
        if (startDate == null) {
            startDate = LocalDate.now().minusDays(7); // Last week by default
        }
        
        ReportResponse response = reportingService.generateWeeklyReport(startDate);
        return ResponseEntity.ok(ApiResponse.success("Weekly report generated successfully", response));
    }

    /**
     * Generate monthly report
     * GET /api/reports/monthly
     */
    @GetMapping("/monthly")
    public ResponseEntity<ApiResponse<ReportResponse>> getMonthlyReport(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {
        
        if (year == null) {
            year = LocalDate.now().getYear();
        }
        if (month == null) {
            month = LocalDate.now().getMonthValue() - 1; // Last month by default
            if (month == 0) {
                month = 12;
                year--;
            }
        }
        
        ReportResponse response = reportingService.generateMonthlyReport(year, month);
        return ResponseEntity.ok(ApiResponse.success("Monthly report generated successfully", response));
    }
}
