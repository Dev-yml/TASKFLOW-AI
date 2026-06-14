package com.arjun.crm.dto.response;

import com.arjun.crm.entity.Project;
import com.arjun.crm.enums.ProjectStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectResponse {
    private Long id;
    private String name;
    private String description;
    private Long workspaceId;
    private String workspaceName;
    private Long createdById;
    private String createdByName;
    private ProjectStatus status;
    private Integer memberCount;
    private Integer taskCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ProjectResponse fromEntity(Project project) {
        if (project == null) return null;
        
        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .workspaceId(project.getWorkspace() != null ? project.getWorkspace().getId() : null)
                .workspaceName(project.getWorkspace() != null ? project.getWorkspace().getName() : null)
                .createdById(project.getCreatedBy() != null ? project.getCreatedBy().getId() : null)
                .createdByName(project.getCreatedBy() != null ? project.getCreatedBy().getFullName() : null)
                .status(project.getStatus())
                .memberCount(project.getMembers() != null ? project.getMembers().size() : 0)
                .taskCount(project.getTasks() != null ? project.getTasks().size() : 0)
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }
}
