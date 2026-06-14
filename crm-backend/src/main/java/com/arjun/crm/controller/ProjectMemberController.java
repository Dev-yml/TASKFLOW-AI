package com.arjun.crm.controller;

import com.arjun.crm.dto.request.AddProjectMemberRequest;
import com.arjun.crm.dto.response.ApiResponse;
import com.arjun.crm.dto.response.ProjectMemberResponse;
import com.arjun.crm.service.ProjectMemberService;
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
@RequestMapping("/api/projects/{projectId}/members")
@RequiredArgsConstructor
@Slf4j
public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;

    /**
     * Add member to project
     * POST /api/projects/{projectId}/members
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ProjectMemberResponse>> addMember(
            @PathVariable Long projectId,
            @Valid @RequestBody AddProjectMemberRequest request) {
        log.info("Add member request received for project ID: {}", projectId);
        ProjectMemberResponse response = projectMemberService.addMember(projectId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Member added successfully", response));
    }

    /**
     * Remove member from project
     * DELETE /api/projects/{projectId}/members/{userId}
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> removeMember(
            @PathVariable Long projectId,
            @PathVariable Long userId) {
        log.info("Remove member request received for project ID: {} and user ID: {}", projectId, userId);
        projectMemberService.removeMember(projectId, userId);
        return ResponseEntity.ok(ApiResponse.success("Member removed successfully", null));
    }

    /**
     * List project members
     * GET /api/projects/{projectId}/members
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProjectMemberResponse>>> listMembers(
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "joinedAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        log.info("List members request received for project ID: {}", projectId);
        
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? 
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<ProjectMemberResponse> response = projectMemberService.listMembers(projectId, pageable);
        return ResponseEntity.ok(ApiResponse.success("Members fetched successfully", response));
    }
}
