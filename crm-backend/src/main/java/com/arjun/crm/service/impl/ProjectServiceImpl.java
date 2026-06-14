package com.arjun.crm.service.impl;

import com.arjun.crm.dto.request.ProjectCreateRequest;
import com.arjun.crm.dto.request.ProjectUpdateRequest;
import com.arjun.crm.dto.response.ProjectResponse;
import com.arjun.crm.entity.Project;
import com.arjun.crm.entity.User;
import com.arjun.crm.entity.Workspace;
import com.arjun.crm.enums.ProjectStatus;
import com.arjun.crm.exception.AccessDeniedException;
import com.arjun.crm.exception.ResourceNotFoundException;
import com.arjun.crm.repository.ProjectMemberRepository;
import com.arjun.crm.repository.ProjectRepository;
import com.arjun.crm.repository.UserRepository;
import com.arjun.crm.repository.WorkspaceMemberRepository;
import com.arjun.crm.repository.WorkspaceRepository;
import com.arjun.crm.service.ProjectService;
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
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Override
    @Transactional
    public ProjectResponse createProject(ProjectCreateRequest request) {
        User currentUser = getAuthenticatedUser();
        log.info("Creating project '{}' in workspace ID: {} by user: {}", 
                request.getName(), request.getWorkspaceId(), currentUser.getEmail());

        Workspace workspace = workspaceRepository.findById(request.getWorkspaceId())
                .orElseThrow(() -> new ResourceNotFoundException("Workspace not found with ID: " + request.getWorkspaceId()));

        // Check if user has access to workspace using repository method
        boolean isOwner = workspace.getOwner().getId().equals(currentUser.getId());
        boolean isMember = workspaceMemberRepository.existsByWorkspaceIdAndUserId(request.getWorkspaceId(), currentUser.getId());
        
        if (!isOwner && !isMember) {
            throw new AccessDeniedException("You don't have access to this workspace");
        }

        Project project = Project.builder()
                .name(request.getName())
                .description(request.getDescription())
                .workspace(workspace)
                .createdBy(currentUser)
                .status(ProjectStatus.ACTIVE)
                .build();

        Project savedProject = projectRepository.save(project);
        log.info("Project created successfully with ID: {}", savedProject.getId());

        return ProjectResponse.fromEntity(savedProject);
    }

    @Override
    @Transactional
    public ProjectResponse updateProject(Long projectId, ProjectUpdateRequest request) {
        User currentUser = getAuthenticatedUser();
        log.info("Updating project ID: {} by user: {}", projectId, currentUser.getEmail());

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + projectId));

        // Check if user has access to workspace using repository method
        Workspace workspace = project.getWorkspace();
        boolean isOwner = workspace.getOwner().getId().equals(currentUser.getId());
        boolean isMember = workspaceMemberRepository.existsByWorkspaceIdAndUserId(workspace.getId(), currentUser.getId());
        
        if (!isOwner && !isMember) {
            throw new AccessDeniedException("You don't have access to this project");
        }

        project.setName(request.getName());
        project.setDescription(request.getDescription());
        if (request.getStatus() != null) {
            project.setStatus(request.getStatus());
        }

        Project updatedProject = projectRepository.save(project);
        log.info("Project updated successfully: {}", projectId);

        return ProjectResponse.fromEntity(updatedProject);
    }

    @Override
    @Transactional
    public void deleteProject(Long projectId) {
        User currentUser = getAuthenticatedUser();
        log.info("Deleting project ID: {} by user: {}", projectId, currentUser.getEmail());

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + projectId));

        // Only workspace owner or project creator can delete
        Workspace workspace = project.getWorkspace();
        boolean canDelete = workspace.getOwner().getId().equals(currentUser.getId()) ||
                project.getCreatedBy().getId().equals(currentUser.getId());

        if (!canDelete) {
            throw new AccessDeniedException("Only workspace owner or project creator can delete project");
        }

        projectRepository.delete(project);
        log.info("Project deleted successfully: {}", projectId);
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponse getProject(Long projectId) {
        User currentUser = getAuthenticatedUser();
        log.info("Fetching project ID: {} for user: {}", projectId, currentUser.getEmail());

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + projectId));

        // Check if user has access to workspace or is project member
        Workspace workspace = project.getWorkspace();
        boolean isWorkspaceOwner = workspace.getOwner().getId().equals(currentUser.getId());
        boolean isWorkspaceMember = workspaceMemberRepository.existsByWorkspaceIdAndUserId(workspace.getId(), currentUser.getId());
        boolean isProjectMember = projectMemberRepository.existsByProjectIdAndUserId(projectId, currentUser.getId());
        
        if (!isWorkspaceOwner && !isWorkspaceMember && !isProjectMember) {
            throw new AccessDeniedException("You don't have access to this project");
        }

        return ProjectResponse.fromEntity(project);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectResponse> listProjectsByWorkspace(Long workspaceId, ProjectStatus status, Pageable pageable) {
        User currentUser = getAuthenticatedUser();
        log.info("Listing projects for workspace ID: {}", workspaceId);

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ResourceNotFoundException("Workspace not found with ID: " + workspaceId));

        // Check if user has access to workspace using repository method
        boolean isOwner = workspace.getOwner().getId().equals(currentUser.getId());
        boolean isMember = workspaceMemberRepository.existsByWorkspaceIdAndUserId(workspaceId, currentUser.getId());
        
        if (!isOwner && !isMember) {
            throw new AccessDeniedException("You don't have access to this workspace");
        }

        Page<Project> projects;
        if (status != null) {
            projects = projectRepository.findByWorkspaceIdAndStatus(workspaceId, status, pageable);
        } else {
            projects = projectRepository.findByWorkspaceId(workspaceId, pageable);
        }

        return projects.map(ProjectResponse::fromEntity);
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
