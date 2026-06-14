package com.arjun.crm.dto.response;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PredictionResponse {
    private Long projectId;
    private String projectName;
    private Double currentProgress;
    private LocalDate expectedCompletionDate;
    private Double confidenceLevel;
    private String delayRisk; // HIGH, MEDIUM, LOW
}
