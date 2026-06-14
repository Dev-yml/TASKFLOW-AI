package com.arjun.crm.service;

import com.arjun.crm.dto.response.TaskAttachmentResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TaskAttachmentService {
    
    /**
     * Upload attachment to task
     */
    TaskAttachmentResponse uploadAttachment(Long taskId, MultipartFile file);
    
    /**
     * Delete attachment
     */
    void deleteAttachment(Long taskId, Long attachmentId);
    
    /**
     * List attachments by task
     */
    List<TaskAttachmentResponse> listAttachments(Long taskId);
    
    /**
     * Get attachment by ID
     */
    TaskAttachmentResponse getAttachment(Long attachmentId);
}
