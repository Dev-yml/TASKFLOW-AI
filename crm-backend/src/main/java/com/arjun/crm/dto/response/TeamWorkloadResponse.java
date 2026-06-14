package com.arjun.crm.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamWorkloadResponse {
    private Long userId;
    private String username;
    private Integer activeTasks;
    private Integer completedTasks;
    private String risk; // HIGH, MEDIUM, LOW
    private String recommendation;
}
