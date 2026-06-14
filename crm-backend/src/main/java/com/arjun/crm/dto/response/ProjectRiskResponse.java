package com.arjun.crm.dto.response;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectRiskResponse {
    private Long projectId;
    private String projectName;
    private String riskLevel; // HIGH, MEDIUM, LOW
    private List<String> reasons;
    private String explanation;
}
