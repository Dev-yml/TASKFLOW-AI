package com.arjun.crm.service.impl;

import com.arjun.crm.dto.response.TaskWatcherResponse;
import com.arjun.crm.entity.Task;
import com.arjun.crm.entity.TaskWatcher;
import com.arjun.crm.entity.User;
import com.arjun.crm.exception.DuplicateMemberException;
import com.arjun.crm.exception.ResourceNotFoundException;
import com.arjun.crm.repository.TaskRepository;
import com.arjun.crm.repository.TaskWatcherRepository;
import com.arjun.crm.repository.UserRepository;
import com.arjun.crm.service.TaskWatcherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskWatcherServiceImpl implements TaskWatcherService {

    private final TaskWatcherRepository taskWatcherRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public TaskWatcherResponse watchTask(Long taskId) {
        User currentUser = getAuthenticatedUser();
        log.info("User {} watching task ID: {}", currentUser.getEmail(), taskId);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId));

        // Check if already watching
        if (taskWatcherRepository.existsByTaskIdAndUserId(taskId, currentUser.getId())) {
            throw new DuplicateMemberException("You are already watching this task");
        }

        TaskWatcher watcher = TaskWatcher.builder()
                .task(task)
                .user(currentUser)
                .build();

        TaskWatcher savedWatcher = taskWatcherRepository.save(watcher);
        log.info("User {} is now watching task: {}", currentUser.getEmail(), taskId);

        return TaskWatcherResponse.fromEntity(savedWatcher);
    }

    @Override
    @Transactional
    public void unwatchTask(Long taskId) {
        User currentUser = getAuthenticatedUser();
        log.info("User {} unwatching task ID: {}", currentUser.getEmail(), taskId);

        // Verify task exists
        taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId));

        TaskWatcher watcher = taskWatcherRepository.findByTaskIdAndUserId(taskId, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("You are not watching this task"));

        taskWatcherRepository.delete(watcher);
        log.info("User {} stopped watching task: {}", currentUser.getEmail(), taskId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskWatcherResponse> listWatchers(Long taskId) {
        log.info("Listing watchers for task ID: {}", taskId);

        // Verify task exists
        taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId));

        List<TaskWatcher> watchers = taskWatcherRepository.findByTaskIdOrderByWatchedAtDesc(taskId);
        return watchers.stream()
                .map(TaskWatcherResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isWatching(Long taskId, Long userId) {
        return taskWatcherRepository.existsByTaskIdAndUserId(taskId, userId);
    }

    /**
     * Get authenticated user from security context
     */
    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }
}
