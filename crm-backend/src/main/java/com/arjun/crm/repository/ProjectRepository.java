package com.arjun.crm.repository;

import com.arjun.crm.entity.Project;
import com.arjun.crm.enums.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    
    Page<Project> findByWorkspaceId(Long workspaceId, Pageable pageable);
    
    java.util.List<Project> findByWorkspaceId(Long workspaceId);
    
    Page<Project> findByWorkspaceIdAndStatus(Long workspaceId, ProjectStatus status, Pageable pageable);
    
    @Query("SELECT DISTINCT p FROM Project p " +
           "LEFT JOIN p.members pm " +
           "WHERE p.workspace.id = :workspaceId " +
           "AND (p.createdBy.id = :userId OR pm.user.id = :userId)")
    Page<Project> findByWorkspaceIdAndUserId(@Param("workspaceId") Long workspaceId, 
                                              @Param("userId") Long userId, 
                                              Pageable pageable);
    
    /**
     * Count projects by status
     */
    Long countByStatus(ProjectStatus status);
    
    /**
     * Count projects created in date range
     */
    @Query("SELECT COUNT(p) FROM Project p WHERE p.createdAt BETWEEN :startDate AND :endDate")
    Long countProjectsCreatedBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    /**
     * Count projects completed in date range
     */
    @Query("SELECT COUNT(p) FROM Project p WHERE p.status = 'COMPLETED' AND p.updatedAt BETWEEN :startDate AND :endDate")
    Long countProjectsCompletedBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
