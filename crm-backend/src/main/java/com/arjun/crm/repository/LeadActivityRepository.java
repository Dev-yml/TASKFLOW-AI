package com.arjun.crm.repository;

import com.arjun.crm.entity.LeadActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeadActivityRepository extends JpaRepository<LeadActivity, Long> {
    
    Page<LeadActivity> findByLeadIdOrderByCreatedAtDesc(Long leadId, Pageable pageable);
    
    List<LeadActivity> findTop10ByLeadIdOrderByCreatedAtDesc(Long leadId);
}
