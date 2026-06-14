package com.arjun.crm.service.impl;

import com.arjun.crm.dto.request.WorkspaceCreateRequest;
import com.arjun.crm.dto.request.WorkspaceUpdateRequest;
import com.arjun.crm.dto.response.WorkspaceResponse;
import com.arjun.crm.entity.User;
import com.arjun.crm.entity.Workspace;
import com.arjun.crm.exception.AccessDeniedException;
import com.arjun.crm.exception.ResourceNotFoundException;
import com.arjun.crm.repository.UserRepository;
import com.arjun.crm.repository.WorkspaceRepository;
import com.arjun.crm.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.arjun.crm.repository.WorkspaceMemberRepository;
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
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;

    @Override
    @Transactional
    public WorkspaceResponse createWorkspace(WorkspaceCreateRequest request) {
        User currentUser = getAuthenticatedUser();
        log.info("Creating workspace '{}' for user: {}", request.getName(), currentUser.getEmail());

        Workspace workspace = Workspace.builder()
                .name(request.getName())
                .description(request.getDescription())
                .owner(currentUser)
                .build();

        Workspace savedWorkspace = workspaceRepository.save(workspace);
        
        // Auto-add the creator as a workspace member with ADMIN role
        // This is crucial because access validation checks the WorkspaceMember table
        com.arjun.crm.entity.WorkspaceMember member = com.arjun.crm.entity.WorkspaceMember.builder()
                .workspace(savedWorkspace)
                .user(currentUser)
                .role(com.arjun.crm.enums.WorkspaceRole.ADMIN)
                .build();
        workspaceMemberRepository.save(member);

        log.info("Workspace created successfully with ID: {}", savedWorkspace.getId());

        return WorkspaceResponse.fromEntity(savedWorkspace);
    }

    @Override
    @Transactional
    public WorkspaceResponse updateWorkspace(Long workspaceId, WorkspaceUpdateRequest request) {
        User currentUser = getAuthenticatedUser();
        log.info("Updating workspace ID: {} by user: {}", workspaceId, currentUser.getEmail());

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ResourceNotFoundException("Workspace not found with ID: " + workspaceId));

        // Only owner can update workspace
        if (!workspace.getOwner().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Only workspace owner can update workspace details");
        }

        workspace.setName(request.getName());
        workspace.setDescription(request.getDescription());

        Workspace updatedWorkspace = workspaceRepository.save(workspace);
        log.info("Workspace updated successfully: {}", workspaceId);

        return WorkspaceResponse.fromEntity(updatedWorkspace);
    }

    @Override
    @Transactional
    public void deleteWorkspace(Long workspaceId) {
        User currentUser = getAuthenticatedUser();
        log.info("Deleting workspace ID: {} by user: {}", workspaceId, currentUser.getEmail());

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ResourceNotFoundException("Workspace not found with ID: " + workspaceId));

        // Only owner can delete workspace
        if (!workspace.getOwner().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Only workspace owner can delete workspace");
        }

        workspaceRepository.delete(workspace);
        log.info("Workspace deleted successfully: {}", workspaceId);
    }

    @Override
    @Transactional(readOnly = true)
    public WorkspaceResponse getWorkspace(Long workspaceId) {
        User currentUser = getAuthenticatedUser();
        log.info("Fetching workspace ID: {} for user: {}", workspaceId, currentUser.getEmail());

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ResourceNotFoundException("Workspace not found with ID: " + workspaceId));

        // Check if user has access (owner or member)
        // Use repository method instead of lazy-loaded collection to ensure accuracy
        boolean isOwner = workspace.getOwner().getId().equals(currentUser.getId());
        boolean isMember = workspaceMemberRepository.existsByWorkspaceIdAndUserId(workspaceId, currentUser.getId());
        
        if (!isOwner && !isMember) {
            throw new AccessDeniedException("You don't have access to this workspace");
        }

        return WorkspaceResponse.fromEntity(workspace);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkspaceResponse> listUserWorkspaces(Pageable pageable) {
        User currentUser = getAuthenticatedUser();
        log.info("Listing workspaces for user: {}", currentUser.getEmail());

        Page<Workspace> workspaces = workspaceRepository
                .findAllByUserIdAsOwnerOrMember(currentUser.getId(), pageable);

        return workspaces.map(WorkspaceResponse::fromEntity);
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
