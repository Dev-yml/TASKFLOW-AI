package com.arjun.crm.service.impl;

import com.arjun.crm.service.CacheEvictionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CacheEvictionServiceImpl implements CacheEvictionService {

    @Override
    @CacheEvict(value = "dashboard", allEntries = true)
    public void evictDashboardCache() {
        log.info("Evicting all dashboard cache entries");
    }

    @Override
    @CacheEvict(value = {"taskAnalytics", "teamPerformance", "activityAnalytics"}, allEntries = true)
    public void evictAnalyticsCache() {
        log.info("Evicting all analytics cache entries");
    }

    @Override
    @CacheEvict(value = {"dailyReport", "weeklyReport", "monthlyReport"}, allEntries = true)
    public void evictReportCache() {
        log.info("Evicting all report cache entries");
    }

    @Override
    @CacheEvict(value = {"dashboard", "taskAnalytics", "teamPerformance", "activityAnalytics", 
                         "dailyReport", "weeklyReport", "monthlyReport"}, allEntries = true)
    public void evictAllCaches() {
        log.info("Evicting all analytics and reporting cache entries");
    }
}
