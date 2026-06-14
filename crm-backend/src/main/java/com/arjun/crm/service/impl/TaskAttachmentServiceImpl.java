package com.arjun.crm.service.impl;

import com.arjun.crm.dto.response.TaskAttachmentResponse;
import com.arjun.crm.entity.Task;
import com.arjun.crm.entity.TaskAttachment;
import com.arjun.crm.entity.User;
import com.arjun.crm.exception.AccessDeniedException;
import com.arjun.crm.exception.ResourceNotFoundException;
import com.arjun.crm.repository.TaskAttachmentRepository;
import com.arjun.crm.repository.TaskRepository;
import com.arjun.crm.repository.UserRepository;
import com.arjun.crm.service.TaskAttachmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskAttachmentServiceImpl implements TaskAttachmentService {

    private final TaskAttachmentRepository taskAttachmentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Value("${file.upload.dir:uploads/task-attachments}")
    private String uploadDir;

    @Value("${file.upload.max-size:10485760}") // 10MB default
    private long maxFileSize;

    @Override
    @Transactional
    public TaskAttachmentResponse uploadAttachment(Long taskId, MultipartFile file) {
        User currentUser = getAuthenticatedUser();
        log.info("Uploading attachment to task ID: {} by user: {}", taskId, currentUser.getEmail());

        // Validate file
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        if (file.getSize() > maxFileSize) {
            throw new IllegalArgumentException("File size exceeds maximum allowed size of " + maxFileSize + " bytes");
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId));

        try {
            // Create upload directory if not exists
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename != null && originalFilename.contains(".") 
                    ? originalFilename.substring(originalFilename.lastIndexOf(".")) 
                    : "";
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

            // Save file to disk
            Path filePath = uploadPath.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Create attachment entity
            TaskAttachment attachment = TaskAttachment.builder()
                    .task(task)
                    .uploadedBy(currentUser)
                    .fileName(originalFilename)
                    .fileUrl("/uploads/task-attachments/" + uniqueFilename)
                    .fileType(file.getContentType())
                    .fileSize(file.getSize())
                    .build();

            TaskAttachment savedAttachment = taskAttachmentRepository.save(attachment);
            log.info("Attachment uploaded successfully: {}", savedAttachment.getId());

            return TaskAttachmentResponse.fromEntity(savedAttachment);

        } catch (IOException e) {
            log.error("Failed to upload file", e);
            throw new RuntimeException("Failed to upload file: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteAttachment(Long taskId, Long attachmentId) {
        User currentUser = getAuthenticatedUser();
        log.info("Deleting attachment ID: {} from task ID: {}", attachmentId, taskId);

        TaskAttachment attachment = taskAttachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Attachment not found with ID: " + attachmentId));

        // Verify attachment belongs to task
        if (!attachment.getTask().getId().equals(taskId)) {
            throw new IllegalArgumentException("Attachment does not belong to this task");
        }

        // Only uploader can delete
        if (!attachment.getUploadedBy().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can only delete your own attachments");
        }

        // Delete file from disk
        try {
            String fileUrl = attachment.getFileUrl();
            String filename = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            Path filePath = Paths.get(uploadDir).resolve(filename);
            Files.deleteIfExists(filePath);
            log.info("File deleted from disk: {}", filename);
        } catch (IOException e) {
            log.error("Failed to delete file from disk", e);
            // Continue with database deletion even if file deletion fails
        }

        taskAttachmentRepository.delete(attachment);
        log.info("Attachment deleted successfully: {}", attachmentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskAttachmentResponse> listAttachments(Long taskId) {
        log.info("Listing attachments for task ID: {}", taskId);

        // Verify task exists
        taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId));

        List<TaskAttachment> attachments = taskAttachmentRepository.findByTaskIdOrderByUploadedAtDesc(taskId);
        return attachments.stream()
                .map(TaskAttachmentResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TaskAttachmentResponse getAttachment(Long attachmentId) {
        log.info("Fetching attachment ID: {}", attachmentId);

        TaskAttachment attachment = taskAttachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Attachment not found with ID: " + attachmentId));

        return TaskAttachmentResponse.fromEntity(attachment);
    }

    /**
     * Get authenticated user from security context
     */
    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }
}
