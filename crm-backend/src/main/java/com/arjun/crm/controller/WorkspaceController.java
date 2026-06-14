package com.arjun.crm.controller;

import com.arjun.crm.dto.request.WorkspaceCreateRequest;
import com.arjun.crm.dto.request.WorkspaceUpdateRequest;
import com.arjun.crm.dto.response.ApiResponse;
import com.arjun.crm.dto.response.WorkspaceResponse;
import com.arjun.crm.service.WorkspaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workspaces")
@RequiredArgsConstructor
@Slf4j
public class WorkspaceController {

    private final WorkspaceService workspaceService;
    private final com.arjun.crm.service.ProjectService projectService;

    /**
     * Create a new workspace
     * POST /api/workspaces
     */
    @PostMapping
    public ResponseEntity<ApiResponse<WorkspaceResponse>> createWorkspace(
            @Valid @RequestBody WorkspaceCreateRequest request) {
        log.info("Create workspace request received: {}", request.getName());
        WorkspaceResponse response = workspaceService.createWorkspace(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Workspace created successfully", response));
    }

    /**
     * Update workspace
     * PUT /api/workspaces/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<WorkspaceResponse>> updateWorkspace(
            @PathVariable Long id,
            @Valid @RequestBody WorkspaceUpdateRequest request) {
        log.info("Update workspace request received for ID: {}", id);
        WorkspaceResponse response = workspaceService.updateWorkspace(id, request);
        return ResponseEntity.ok(ApiResponse.success("Workspace updated successfully", response));
    }

    /**
     * Delete workspace
     * DELETE /api/workspaces/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteWorkspace(@PathVariable Long id) {
        log.info("Delete workspace request received for ID: {}", id);
        workspaceService.deleteWorkspace(id);
        return ResponseEntity.ok(ApiResponse.success("Workspace deleted successfully", null));
    }

    /**
     * Get workspace details
     * GET /api/workspaces/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WorkspaceResponse>> getWorkspace(@PathVariable Long id) {
        log.info("Get workspace request received for ID: {}", id);
        WorkspaceResponse response = workspaceService.getWorkspace(id);
        return ResponseEntity.ok(ApiResponse.success("Workspace fetched successfully", response));
    }

    /**
     * List all workspaces for current user
     * GET /api/workspaces
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<WorkspaceResponse>>> listWorkspaces(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        log.info("List workspaces request received");
        
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? 
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<WorkspaceResponse> response = workspaceService.listUserWorkspaces(pageable);
        return ResponseEntity.ok(ApiResponse.success("Workspaces fetched successfully", response));
    }

    /**
     * List projects by workspace
     * GET /api/workspaces/{workspaceId}/projects
     */
    @GetMapping("/{workspaceId}/projects")
    public ResponseEntity<ApiResponse<Page<com.arjun.crm.dto.response.ProjectResponse>>> listProjectsByWorkspace(
            @PathVariable Long workspaceId,
            @RequestParam(required = false) com.arjun.crm.enums.ProjectStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        log.info("List projects request received via WorkspaceController for workspace ID: {}", workspaceId);
        
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? 
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<com.arjun.crm.dto.response.ProjectResponse> response = projectService.listProjectsByWorkspace(workspaceId, status, pageable);
        return ResponseEntity.ok(ApiResponse.success("Projects fetched successfully", response));
    }
}
