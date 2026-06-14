package com.arjun.crm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeadAnalyticsResponse {
    
    private Long totalLeads;
    private BigDecimal totalPipelineValue;
    private BigDecimal wonValue;
    private BigDecimal lostValue;
    private Double conversionRate;
    private Map<String, Long> leadsByStatus;
    private Map<String, BigDecimal> valueByStatus;
    private List<AssigneePerformance> assigneePerformance;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AssigneePerformance {
        private Long userId;
        private String userName;
        private Long leadsCount;
        private BigDecimal totalValue;
    }
}
