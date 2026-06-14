package com.arjun.crm.controller;

import com.arjun.crm.dto.response.ApiResponse;
import com.arjun.crm.dto.response.TaskActivityResponse;
import com.arjun.crm.service.TaskActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks/{taskId}/activities")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class TaskActivityController {

    private final TaskActivityService taskActivityService;

    /**
     * Get task activities
     * GET /api/tasks/{taskId}/activities
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<TaskActivityResponse>>> getTaskActivities(
            @PathVariable Long taskId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<TaskActivityResponse> activities = taskActivityService.getTaskActivities(taskId, pageable);
        return ResponseEntity.ok(ApiResponse.success("Activities retrieved successfully", activities));
    }
}
