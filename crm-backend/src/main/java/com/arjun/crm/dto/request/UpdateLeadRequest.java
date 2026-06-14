package com.arjun.crm.dto.request;

import com.arjun.crm.enums.LeadPriority;
import com.arjun.crm.enums.LeadStatus;
import jakarta.validation.constraints.Email;
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
public class UpdateLeadRequest {
    
    private String name;
    
    @Email(message = "Invalid email format")
    private String email;
    
    private String phone;
    
    private String company;
    
    private String position;
    
    private BigDecimal dealValue;
    
    private LeadStatus status;
    
    private LeadPriority priority;
    
    private Long assignedToId;
    
    private List<String> tags;
    
    private String notes;
    
    private LocalDate expectedCloseDate;
}
