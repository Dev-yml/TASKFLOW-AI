package com.arjun.crm.dto.response;

import com.arjun.crm.entity.ProjectMember;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectMemberResponse {
    private Long id;
    private Long userId;
    private String userName;
    private String userEmail;
    private LocalDateTime joinedAt;

    public static ProjectMemberResponse fromEntity(ProjectMember member) {
        return ProjectMemberResponse.builder()
                .id(member.getId())
                .userId(member.getUser().getId())
                .userName(member.getUser().getFullName())
                .userEmail(member.getUser().getEmail())
                .joinedAt(member.getJoinedAt())
                .build();
    }
}
