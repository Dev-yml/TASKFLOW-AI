package com.arjun.crm.dto.request;

import com.arjun.crm.enums.LeadStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateLeadStatusRequest {
    
    @NotNull(message = "Status is required")
    private LeadStatus status;
    
    private String notes;
}
