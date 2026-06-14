package com.arjun.crm.service.impl;

import com.arjun.crm.dto.request.AddProjectMemberRequest;
import com.arjun.crm.dto.response.ProjectMemberResponse;
import com.arjun.crm.entity.Project;
import com.arjun.crm.entity.ProjectMember;
import com.arjun.crm.entity.User;
import com.arjun.crm.entity.Workspace;
import com.arjun.crm.exception.AccessDeniedException;
import com.arjun.crm.exception.DuplicateMemberException;
import com.arjun.crm.exception.ResourceNotFoundException;
import com.arjun.crm.repository.ProjectMemberRepository;
import com.arjun.crm.repository.ProjectRepository;
import com.arjun.crm.repository.UserRepository;
import com.arjun.crm.repository.WorkspaceMemberRepository;
import com.arjun.crm.service.ProjectMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectMemberServiceImpl implements ProjectMemberService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final UserRepository userRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;

    @Override
    @Transactional
    public ProjectMemberResponse addMember(Long projectId, AddProjectMemberRequest request) {
        User currentUser = getAuthenticatedUser();
        log.info("Adding member to project ID: {} by user: {}", projectId, currentUser.getEmail());

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + projectId));

        // Check if current user has access to workspace using repository method
        Workspace workspace = project.getWorkspace();
        boolean isOwner = workspace.getOwner().getId().equals(currentUser.getId());
        boolean isMember = workspaceMemberRepository.existsByWorkspaceIdAndUserId(workspace.getId(), currentUser.getId());
        
        if (!isOwner && !isMember) {
            throw new AccessDeniedException("You don't have access to this project");
        }

        // Check if user to be added exists
        User userToAdd = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + request.getUserId()));

        // Check if user is already a member
        if (projectMemberRepository.existsByProjectIdAndUserId(projectId, request.getUserId())) {
            throw new DuplicateMemberException("User is already a member of this project");
        }

        // User must be a member of the workspace using repository method
        boolean isWorkspaceOwner = workspace.getOwner().getId().equals(request.getUserId());
        boolean isWorkspaceMember = workspaceMemberRepository.existsByWorkspaceIdAndUserId(workspace.getId(), request.getUserId());

        if (!isWorkspaceOwner && !isWorkspaceMember) {
            throw new IllegalArgumentException("User must be a member of the workspace first");
        }

        ProjectMember member = ProjectMember.builder()
                .project(project)
                .user(userToAdd)
                .build();

        ProjectMember savedMember = projectMemberRepository.save(member);
        log.info("Member added successfully to project: {}", projectId);

        return ProjectMemberResponse.fromEntity(savedMember);
    }

    @Override
    @Transactional
    public void removeMember(Long projectId, Long userId) {
        User currentUser = getAuthenticatedUser();
        log.info("Removing member from project ID: {} by user: {}", projectId, currentUser.getEmail());

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + projectId));

        // Check if current user has access to workspace using repository method
        Workspace workspace = project.getWorkspace();
        boolean isOwner = workspace.getOwner().getId().equals(currentUser.getId());
        boolean isMember = workspaceMemberRepository.existsByWorkspaceIdAndUserId(workspace.getId(), currentUser.getId());
        
        if (!isOwner && !isMember) {
            throw new AccessDeniedException("You don't have access to this project");
        }

        ProjectMember member = projectMemberRepository.findByProjectIdAndUserId(projectId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found in this project"));

        projectMemberRepository.delete(member);
        log.info("Member removed successfully from project: {}", projectId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectMemberResponse> listMembers(Long projectId, Pageable pageable) {
        User currentUser = getAuthenticatedUser();
        log.info("Listing members for project ID: {}", projectId);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + projectId));

        // Check if user has access to workspace or is project member using repository methods
        Workspace workspace = project.getWorkspace();
        boolean isWorkspaceOwner = workspace.getOwner().getId().equals(currentUser.getId());
        boolean isWorkspaceMember = workspaceMemberRepository.existsByWorkspaceIdAndUserId(workspace.getId(), currentUser.getId());
        boolean isProjectMember = projectMemberRepository.existsByProjectIdAndUserId(projectId, currentUser.getId());
        
        if (!isWorkspaceOwner && !isWorkspaceMember && !isProjectMember) {
            throw new AccessDeniedException("You don't have access to this project");
        }

        Page<ProjectMember> members = projectMemberRepository.findByProjectId(projectId, pageable);
        return members.map(ProjectMemberResponse::fromEntity);
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
