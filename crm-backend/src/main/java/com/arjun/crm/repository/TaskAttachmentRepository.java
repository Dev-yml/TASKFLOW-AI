package com.arjun.crm.repository;

import com.arjun.crm.entity.TaskAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskAttachmentRepository extends JpaRepository<TaskAttachment, Long> {
    
    List<TaskAttachment> findByTaskIdOrderByUploadedAtDesc(Long taskId);
    
    void deleteByTaskId(Long taskId);
}
