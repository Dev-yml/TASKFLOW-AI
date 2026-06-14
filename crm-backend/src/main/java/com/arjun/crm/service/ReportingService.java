package com.arjun.crm.service;

import com.arjun.crm.dto.response.ReportResponse;

import java.time.LocalDate;

public interface ReportingService {
    
    /**
     * Generate daily report
     */
    ReportResponse generateDailyReport(LocalDate date);
    
    /**
     * Generate weekly report
     */
    ReportResponse generateWeeklyReport(LocalDate startDate);
    
    /**
     * Generate monthly report
     */
    ReportResponse generateMonthlyReport(int year, int month);
}
