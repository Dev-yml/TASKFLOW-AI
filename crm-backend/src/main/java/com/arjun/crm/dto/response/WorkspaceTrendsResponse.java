package com.arjun.crm.dto.response;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkspaceTrendsResponse {
    private List<LocalDate> dates;
    private List<Integer> healthScores;
    private List<Integer> overdueTasks;
    private List<Double> leadConversions;
}
