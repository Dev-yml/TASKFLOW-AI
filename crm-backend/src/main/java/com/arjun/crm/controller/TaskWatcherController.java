package com.arjun.crm.controller;

import com.arjun.crm.dto.response.ApiResponse;
import com.arjun.crm.dto.response.TaskWatcherResponse;
import com.arjun.crm.service.TaskWatcherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks/{taskId}/watchers")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class TaskWatcherController {

    private final TaskWatcherService taskWatcherService;

    /**
     * Watch a task
     * POST /api/tasks/{taskId}/watchers
     */
    @PostMapping
    public ResponseEntity<ApiResponse<TaskWatcherResponse>> watchTask(@PathVariable Long taskId) {
        TaskWatcherResponse response = taskWatcherService.watchTask(taskId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("You are now watching this task", response));
    }

    /**
     * Unwatch a task
     * DELETE /api/tasks/{taskId}/watchers
     */
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> unwatchTask(@PathVariable Long taskId) {
        taskWatcherService.unwatchTask(taskId);
        return ResponseEntity.ok(ApiResponse.success("You stopped watching this task", null));
    }

    /**
     * List task watchers
     * GET /api/tasks/{taskId}/watchers
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<TaskWatcherResponse>>> listWatchers(@PathVariable Long taskId) {
        List<TaskWatcherResponse> watchers = taskWatcherService.listWatchers(taskId);
        return ResponseEntity.ok(ApiResponse.success("Watchers retrieved successfully", watchers));
    }
}
