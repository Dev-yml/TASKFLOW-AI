package com.arjun.crm.controller;

import com.arjun.crm.dto.request.TaskCommentCreateRequest;
import com.arjun.crm.dto.request.TaskCommentUpdateRequest;
import com.arjun.crm.dto.response.ApiResponse;
import com.arjun.crm.dto.response.TaskCommentResponse;
import com.arjun.crm.service.TaskCommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks/{taskId}/comments")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class TaskCommentController {

    private final TaskCommentService taskCommentService;

    /**
     * Add comment to task
     * POST /api/tasks/{taskId}/comments
     */
    @PostMapping
    public ResponseEntity<ApiResponse<TaskCommentResponse>> addComment(
            @PathVariable Long taskId,
            @Valid @RequestBody TaskCommentCreateRequest request) {
        
        TaskCommentResponse response = taskCommentService.addComment(taskId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Comment added successfully", response));
    }

    /**
     * Update comment
     * PUT /api/tasks/{taskId}/comments/{commentId}
     */
    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<TaskCommentResponse>> updateComment(
            @PathVariable Long taskId,
            @PathVariable Long commentId,
            @Valid @RequestBody TaskCommentUpdateRequest request) {
        
        TaskCommentResponse response = taskCommentService.updateComment(taskId, commentId, request);
        return ResponseEntity.ok(ApiResponse.success("Comment updated successfully", response));
    }

    /**
     * Delete comment
     * DELETE /api/tasks/{taskId}/comments/{commentId}
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long taskId,
            @PathVariable Long commentId) {
        
        taskCommentService.deleteComment(taskId, commentId);
        return ResponseEntity.ok(ApiResponse.success("Comment deleted successfully", null));
    }

    /**
     * Get comments by task
     * GET /api/tasks/{taskId}/comments
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<TaskCommentResponse>>> getCommentsByTask(
            @PathVariable Long taskId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<TaskCommentResponse> comments = taskCommentService.getCommentsByTask(taskId, pageable);
        return ResponseEntity.ok(ApiResponse.success("Comments retrieved successfully", comments));
    }
}
