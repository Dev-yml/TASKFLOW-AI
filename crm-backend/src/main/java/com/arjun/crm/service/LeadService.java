package com.arjun.crm.service;

import com.arjun.crm.dto.request.CreateLeadRequest;
import com.arjun.crm.dto.request.UpdateLeadRequest;
import com.arjun.crm.dto.request.UpdateLeadStatusRequest;
import com.arjun.crm.dto.response.LeadActivityResponse;
import com.arjun.crm.dto.response.LeadAnalyticsResponse;
import com.arjun.crm.dto.response.LeadResponse;
import com.arjun.crm.enums.LeadStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LeadService {
    
    LeadResponse createLead(CreateLeadRequest request, Long userId);
    
    LeadResponse updateLead(Long leadId, UpdateLeadRequest request, Long userId);
    
    LeadResponse updateLeadStatus(Long leadId, UpdateLeadStatusRequest request, Long userId);
    
    LeadResponse getLeadById(Long leadId, Long userId);
    
    Page<LeadResponse> getLeadsByWorkspace(Long workspaceId, Long userId, Pageable pageable);
    
    Page<LeadResponse> getLeadsByFilters(Long workspaceId, LeadStatus status, Long assignedToId, 
                                         String priority, String search, Long userId, Pageable pageable);
    
    void deleteLead(Long leadId, Long userId);
    
    List<LeadActivityResponse> getLeadActivities(Long leadId, Long userId);
    
    LeadAnalyticsResponse getLeadAnalytics(Long workspaceId, Long userId);
}
