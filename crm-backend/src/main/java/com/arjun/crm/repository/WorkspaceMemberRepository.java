package com.arjun.crm.repository;

import com.arjun.crm.entity.WorkspaceMember;
import com.arjun.crm.enums.WorkspaceRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMember, Long> {
    
    boolean existsByWorkspaceIdAndUserId(Long workspaceId, Long userId);
    
    Optional<WorkspaceMember> findByWorkspaceIdAndUserId(Long workspaceId, Long userId);
    
    @Query("SELECT wm FROM WorkspaceMember wm WHERE wm.workspace.id = :workspaceId")
    Page<WorkspaceMember> findByWorkspaceId(@Param("workspaceId") Long workspaceId, Pageable pageable);
    
    void deleteByWorkspaceIdAndUserId(Long workspaceId, Long userId);
    
    @Query("SELECT CASE WHEN COUNT(wm) > 0 THEN true ELSE false END " +
           "FROM WorkspaceMember wm " +
           "WHERE wm.workspace.id = :workspaceId AND wm.user.id = :userId AND wm.role = :role")
    boolean isUserAdminOfWorkspace(@Param("workspaceId") Long workspaceId,
                                    @Param("userId") Long userId,
                                    @Param("role") WorkspaceRole role);
    
    default boolean isUserAdminOfWorkspace(Long workspaceId, Long userId) {
        return isUserAdminOfWorkspace(workspaceId, userId, WorkspaceRole.ADMIN);
    }
}
