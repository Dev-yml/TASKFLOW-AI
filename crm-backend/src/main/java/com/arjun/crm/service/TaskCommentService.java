package com.arjun.crm.service;

import com.arjun.crm.dto.request.TaskCommentCreateRequest;
import com.arjun.crm.dto.request.TaskCommentUpdateRequest;
import com.arjun.crm.dto.response.TaskCommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskCommentService {
    
    /**
     * Add comment to task
     */
    TaskCommentResponse addComment(Long taskId, TaskCommentCreateRequest request);
    
    /**
     * Update comment
     */
    TaskCommentResponse updateComment(Long taskId, Long commentId, TaskCommentUpdateRequest request);
    
    /**
     * Delete comment
     */
    void deleteComment(Long taskId, Long commentId);
    
    /**
     * Get comments by task
     */
    Page<TaskCommentResponse> getCommentsByTask(Long taskId, Pageable pageable);
}
