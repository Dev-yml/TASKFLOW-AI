package com.arjun.crm.dto.request;

import com.arjun.crm.enums.LeadPriority;
import com.arjun.crm.enums.LeadStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateLeadRequest {
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    private String phone;
    
    private String company;
    
    private String position;
    
    private BigDecimal dealValue;
    
    @NotNull(message = "Status is required")
    private LeadStatus status;
    
    @NotNull(message = "Priority is required")
    private LeadPriority priority;
    
    private Long assignedToId;
    
    @NotNull(message = "Workspace ID is required")
    private Long workspaceId;
    
    private List<String> tags;
    
    private String notes;
    
    private LocalDate expectedCloseDate;
}
