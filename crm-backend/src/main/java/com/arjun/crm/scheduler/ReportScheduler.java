package com.arjun.crm.scheduler;

import com.arjun.crm.dto.response.ReportResponse;
import com.arjun.crm.service.ReportingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Scheduler for automatic report generation
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ReportScheduler {

    private final ReportingService reportingService;

    /**
     * Generate daily report at 11:59 PM every day
     */
    @Scheduled(cron = "0 59 23 * * *")
    public void generateDailyReport() {
        try {
            log.info("Starting scheduled daily report generation");
            LocalDate today = LocalDate.now();
            ReportResponse report = reportingService.generateDailyReport(today);
            log.info("Daily report generated successfully for {}", today);
            // Report can be stored in database or sent via email
        } catch (Exception e) {
            log.error("Error generating daily report", e);
        }
    }

    /**
     * Generate weekly report every Monday at 12:00 AM
     */
    @Scheduled(cron = "0 0 0 * * MON")
    public void generateWeeklyReport() {
        try {
            log.info("Starting scheduled weekly report generation");
            LocalDate startOfWeek = LocalDate.now().minusDays(7);
            ReportResponse report = reportingService.generateWeeklyReport(startOfWeek);
            log.info("Weekly report generated successfully for week starting {}", startOfWeek);
            // Report can be stored in database or sent via email
        } catch (Exception e) {
            log.error("Error generating weekly report", e);
        }
    }

    /**
     * Generate monthly report on the 1st day of every month at 12:00 AM
     */
    @Scheduled(cron = "0 0 0 1 * *")
    public void generateMonthlyReport() {
        try {
            log.info("Starting scheduled monthly report generation");
            LocalDate lastMonth = LocalDate.now().minusMonths(1);
            int year = lastMonth.getYear();
            int month = lastMonth.getMonthValue();
            ReportResponse report = reportingService.generateMonthlyReport(year, month);
            log.info("Monthly report generated successfully for {}-{}", year, month);
            // Report can be stored in database or sent via email
        } catch (Exception e) {
            log.error("Error generating monthly report", e);
        }
    }
}
