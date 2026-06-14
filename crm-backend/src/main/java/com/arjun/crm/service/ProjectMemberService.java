package com.arjun.crm.service;

import com.arjun.crm.dto.request.AddProjectMemberRequest;
import com.arjun.crm.dto.response.ProjectMemberResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectMemberService {
    
    /**
     * Add member to project
     * @param projectId project ID
     * @param request member details
     * @return added member response
     */
    ProjectMemberResponse addMember(Long projectId, AddProjectMemberRequest request);
    
    /**
     * Remove member from project
     * @param projectId project ID
     * @param userId user ID to remove
     */
    void removeMember(Long projectId, Long userId);
    
    /**
     * List project members
     * @param projectId project ID
     * @param pageable pagination details
     * @return page of members
     */
    Page<ProjectMemberResponse> listMembers(Long projectId, Pageable pageable);
}
