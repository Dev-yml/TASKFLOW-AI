package com.arjun.crm.service;

/**
 * Service for cache eviction operations
 */
public interface CacheEvictionService {
    
    /**
     * Evict all dashboard caches
     */
    void evictDashboardCache();
    
    /**
     * Evict all analytics caches
     */
    void evictAnalyticsCache();
    
    /**
     * Evict all report caches
     */
    void evictReportCache();
    
    /**
     * Evict all caches
     */
    void evictAllCaches();
}
