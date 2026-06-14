package com.arjun.crm.service;

import com.arjun.crm.dto.request.WorkspaceCreateRequest;
import com.arjun.crm.dto.request.WorkspaceUpdateRequest;
import com.arjun.crm.dto.response.WorkspaceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WorkspaceService {
    
    /**
     * Create a new workspace
     * @param request workspace creation details
     * @return created workspace response
     */
    WorkspaceResponse createWorkspace(WorkspaceCreateRequest request);
    
    /**
     * Update workspace details
     * @param workspaceId workspace ID
     * @param request update details
     * @return updated workspace response
     */
    WorkspaceResponse updateWorkspace(Long workspaceId, WorkspaceUpdateRequest request);
    
    /**
     * Delete workspace
     * @param workspaceId workspace ID to delete
     */
    void deleteWorkspace(Long workspaceId);
    
    /**
     * Get workspace details
     * @param workspaceId workspace ID
     * @return workspace response
     */
    WorkspaceResponse getWorkspace(Long workspaceId);
    
    /**
     * List all workspaces for current user
     * @param pageable pagination details
     * @return page of workspaces
     */
    Page<WorkspaceResponse> listUserWorkspaces(Pageable pageable);
}
