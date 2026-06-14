package com.arjun.crm.service.impl;

import com.arjun.crm.dto.request.AddWorkspaceMemberRequest;
import com.arjun.crm.dto.response.WorkspaceMemberResponse;
import com.arjun.crm.entity.User;
import com.arjun.crm.entity.Workspace;
import com.arjun.crm.entity.WorkspaceMember;
import com.arjun.crm.enums.WorkspaceRole;
import com.arjun.crm.exception.AccessDeniedException;
import com.arjun.crm.exception.DuplicateMemberException;
import com.arjun.crm.exception.ResourceNotFoundException;
import com.arjun.crm.repository.UserRepository;
import com.arjun.crm.repository.WorkspaceMemberRepository;
import com.arjun.crm.repository.WorkspaceRepository;
import com.arjun.crm.service.WorkspaceMemberService;
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
public class WorkspaceMemberServiceImpl implements WorkspaceMemberService {

    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public WorkspaceMemberResponse addMember(Long workspaceId, AddWorkspaceMemberRequest request) {
        User currentUser = getAuthenticatedUser();
        log.info("Adding member to workspace ID: {} by user: {}", workspaceId, currentUser.getEmail());

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ResourceNotFoundException("Workspace not found with ID: " + workspaceId));

        // Check if current user is owner or admin
        if (!isOwnerOrAdmin(workspace, currentUser)) {
            throw new AccessDeniedException("Only workspace owner or admin can add members");
        }

        // Check if user to be added exists
        User userToAdd = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + request.getUserId()));

        // Check if user is already a member
        if (workspaceMemberRepository.existsByWorkspaceIdAndUserId(workspaceId, request.getUserId())) {
            throw new DuplicateMemberException("User is already a member of this workspace");
        }

        // Cannot add owner as member
        if (workspace.getOwner().getId().equals(request.getUserId())) {
            throw new IllegalArgumentException("Workspace owner cannot be added as a member");
        }

        WorkspaceMember member = WorkspaceMember.builder()
                .workspace(workspace)
                .user(userToAdd)
                .role(request.getRole())
                .build();

        WorkspaceMember savedMember = workspaceMemberRepository.save(member);
        log.info("Member added successfully to workspace: {}", workspaceId);

        return WorkspaceMemberResponse.fromEntity(savedMember);
    }

    @Override
    @Transactional
    public void removeMember(Long workspaceId, Long userId) {
        User currentUser = getAuthenticatedUser();
        log.info("Removing member from workspace ID: {} by user: {}", workspaceId, currentUser.getEmail());

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ResourceNotFoundException("Workspace not found with ID: " + workspaceId));

        // Check if current user is owner or admin
        if (!isOwnerOrAdmin(workspace, currentUser)) {
            throw new AccessDeniedException("Only workspace owner or admin can remove members");
        }

        // Cannot remove owner
        if (workspace.getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("Cannot remove workspace owner");
        }

        WorkspaceMember member = workspaceMemberRepository.findByWorkspaceIdAndUserId(workspaceId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found in this workspace"));

        workspaceMemberRepository.delete(member);
        log.info("Member removed successfully from workspace: {}", workspaceId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkspaceMemberResponse> listMembers(Long workspaceId, Pageable pageable) {
        User currentUser = getAuthenticatedUser();
        log.info("Listing members for workspace ID: {}", workspaceId);

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ResourceNotFoundException("Workspace not found with ID: " + workspaceId));

        // Check if user has access to workspace (owner or member)
        // Use repository method instead of lazy-loaded collection to ensure accuracy
        boolean isOwner = workspace.getOwner().getId().equals(currentUser.getId());
        boolean isMember = workspaceMemberRepository.existsByWorkspaceIdAndUserId(workspaceId, currentUser.getId());
        
        if (!isOwner && !isMember) {
            throw new AccessDeniedException("You don't have access to this workspace");
        }

        Page<WorkspaceMember> members = workspaceMemberRepository.findByWorkspaceId(workspaceId, pageable);
        return members.map(WorkspaceMemberResponse::fromEntity);
    }

    /**
     * Check if user is owner or admin of workspace
     */
    private boolean isOwnerOrAdmin(Workspace workspace, User user) {
        // Owner always has access
        if (workspace.getOwner().getId().equals(user.getId())) {
            return true;
        }

        // Check if user is admin member using repository (avoids lazy loading issues)
        return workspaceMemberRepository.isUserAdminOfWorkspace(workspace.getId(), user.getId());
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
