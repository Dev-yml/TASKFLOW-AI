package com.arjun.crm.controller;

import com.arjun.crm.dto.response.ApiResponse;
import com.arjun.crm.dto.response.TaskAttachmentResponse;
import com.arjun.crm.service.TaskAttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/tasks/{taskId}/attachments")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class TaskAttachmentController {

    private final TaskAttachmentService taskAttachmentService;

    /**
     * Upload attachment to task
     * POST /api/tasks/{taskId}/attachments
     */
    @PostMapping
    public ResponseEntity<ApiResponse<TaskAttachmentResponse>> uploadAttachment(
            @PathVariable Long taskId,
            @RequestParam("file") MultipartFile file) {
        
        TaskAttachmentResponse response = taskAttachmentService.uploadAttachment(taskId, file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Attachment uploaded successfully", response));
    }

    /**
     * Delete attachment
     * DELETE /api/tasks/{taskId}/attachments/{attachmentId}
     */
    @DeleteMapping("/{attachmentId}")
    public ResponseEntity<ApiResponse<Void>> deleteAttachment(
            @PathVariable Long taskId,
            @PathVariable Long attachmentId) {
        
        taskAttachmentService.deleteAttachment(taskId, attachmentId);
        return ResponseEntity.ok(ApiResponse.success("Attachment deleted successfully", null));
    }

    /**
     * List attachments by task
     * GET /api/tasks/{taskId}/attachments
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<TaskAttachmentResponse>>> listAttachments(
            @PathVariable Long taskId) {
        
        List<TaskAttachmentResponse> attachments = taskAttachmentService.listAttachments(taskId);
        return ResponseEntity.ok(ApiResponse.success("Attachments retrieved successfully", attachments));
    }

    /**
     * Get attachment by ID
     * GET /api/tasks/{taskId}/attachments/{attachmentId}
     */
    @GetMapping("/{attachmentId}")
    public ResponseEntity<ApiResponse<TaskAttachmentResponse>> getAttachment(
            @PathVariable Long taskId,
            @PathVariable Long attachmentId) {
        
        TaskAttachmentResponse attachment = taskAttachmentService.getAttachment(attachmentId);
        return ResponseEntity.ok(ApiResponse.success("Attachment retrieved successfully", attachment));
    }
}
