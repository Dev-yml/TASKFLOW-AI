package com.arjun.crm.service.impl;

import com.arjun.crm.dto.response.TaskActivityResponse;
import com.arjun.crm.entity.Task;
import com.arjun.crm.entity.TaskActivity;
import com.arjun.crm.entity.User;
import com.arjun.crm.exception.ResourceNotFoundException;
import com.arjun.crm.repository.TaskActivityRepository;
import com.arjun.crm.repository.TaskRepository;
import com.arjun.crm.service.TaskActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskActivityServiceImpl implements TaskActivityService {

    private final TaskActivityRepository taskActivityRepository;
    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public void logTaskCreation(Task task, User user) {
        log.info("Logging task creation for task ID: {}", task.getId());
        
        TaskActivity activity = TaskActivity.builder()
                .task(task)
                .user(user)
                .action("TASK_CREATED")
                .newValue("Task created: " + task.getTitle())
                .build();
        
        taskActivityRepository.save(activity);
    }

    @Override
    @Transactional
    public void logStatusChange(Task task, User user, String oldStatus, String newStatus) {
        log.info("Logging status change for task ID: {} from {} to {}", task.getId(), oldStatus, newStatus);
        
        TaskActivity activity = TaskActivity.builder()
                .task(task)
                .user(user)
                .action("STATUS_CHANGED")
                .oldValue(oldStatus)
                .newValue(newStatus)
                .build();
        
        taskActivityRepository.save(activity);
    }

    @Override
    @Transactional
    public void logPriorityChange(Task task, User user, String oldPriority, String newPriority) {
        log.info("Logging priority change for task ID: {} from {} to {}", task.getId(), oldPriority, newPriority);
        
        TaskActivity activity = TaskActivity.builder()
                .task(task)
                .user(user)
                .action("PRIORITY_CHANGED")
                .oldValue(oldPriority)
                .newValue(newPriority)
                .build();
        
        taskActivityRepository.save(activity);
    }

    @Override
    @Transactional
    public void logAssignmentChange(Task task, User user, String oldAssignee, String newAssignee) {
        log.info("Logging assignment change for task ID: {}", task.getId());
        
        TaskActivity activity = TaskActivity.builder()
                .task(task)
                .user(user)
                .action("ASSIGNMENT_CHANGED")
                .oldValue(oldAssignee != null ? oldAssignee : "Unassigned")
                .newValue(newAssignee != null ? newAssignee : "Unassigned")
                .build();
        
        taskActivityRepository.save(activity);
    }

    @Override
    @Transactional
    public void logDueDateChange(Task task, User user, String oldDueDate, String newDueDate) {
        log.info("Logging due date change for task ID: {}", task.getId());
        
        TaskActivity activity = TaskActivity.builder()
                .task(task)
                .user(user)
                .action("DUE_DATE_CHANGED")
                .oldValue(oldDueDate != null ? oldDueDate : "No due date")
                .newValue(newDueDate != null ? newDueDate : "No due date")
                .build();
        
        taskActivityRepository.save(activity);
    }

    @Override
    @Transactional
    public void logCommentCreation(Task task, User user) {
        log.info("Logging comment creation for task ID: {}", task.getId());
        
        TaskActivity activity = TaskActivity.builder()
                .task(task)
                .user(user)
                .action("COMMENT_ADDED")
                .newValue("Comment added by " + user.getFullName())
                .build();
        
        taskActivityRepository.save(activity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskActivityResponse> getTaskActivities(Long taskId, Pageable pageable) {
        log.info("Fetching activities for task ID: {}", taskId);
        
        // Verify task exists
        taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId));
        
        Page<TaskActivity> activities = taskActivityRepository.findByTaskIdOrderByCreatedAtDesc(taskId, pageable);
        return activities.map(TaskActivityResponse::fromEntity);
    }
}
