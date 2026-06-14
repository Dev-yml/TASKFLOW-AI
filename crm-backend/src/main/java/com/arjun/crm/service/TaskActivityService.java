package com.arjun.crm.service;

import com.arjun.crm.dto.response.TaskActivityResponse;
import com.arjun.crm.entity.Task;
import com.arjun.crm.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskActivityService {
    
    /**
     * Log task creation activity
     */
    void logTaskCreation(Task task, User user);
    
    /**
     * Log task status change
     */
    void logStatusChange(Task task, User user, String oldStatus, String newStatus);
    
    /**
     * Log task priority change
     */
    void logPriorityChange(Task task, User user, String oldPriority, String newPriority);
    
    /**
     * Log task assignment change
     */
    void logAssignmentChange(Task task, User user, String oldAssignee, String newAssignee);
    
    /**
     * Log due date change
     */
    void logDueDateChange(Task task, User user, String oldDueDate, String newDueDate);
    
    /**
     * Log comment creation
     */
    void logCommentCreation(Task task, User user);
    
    /**
     * Get task activities
     */
    Page<TaskActivityResponse> getTaskActivities(Long taskId, Pageable pageable);
}
