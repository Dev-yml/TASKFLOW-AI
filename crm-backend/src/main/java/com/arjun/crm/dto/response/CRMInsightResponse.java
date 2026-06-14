package com.arjun.crm.dto.response;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CRMInsightResponse {
    private Long leadId;
    private String leadName;
    private String company;
    private Integer lastFollowUpDays;
    private BigDecimal dealValue;
    private String status; // e.g. "Inactive Lead", "Potential Lost Lead", "High Value Opportunity"
    private String suggestedAction;
}
