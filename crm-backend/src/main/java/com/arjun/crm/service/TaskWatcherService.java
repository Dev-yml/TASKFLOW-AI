package com.arjun.crm.service;

import com.arjun.crm.dto.response.TaskWatcherResponse;

import java.util.List;

public interface TaskWatcherService {
    
    /**
     * Watch a task
     */
    TaskWatcherResponse watchTask(Long taskId);
    
    /**
     * Unwatch a task
     */
    void unwatchTask(Long taskId);
    
    /**
     * List task watchers
     */
    List<TaskWatcherResponse> listWatchers(Long taskId);
    
    /**
     * Check if user is watching task
     */
    boolean isWatching(Long taskId, Long userId);
}
