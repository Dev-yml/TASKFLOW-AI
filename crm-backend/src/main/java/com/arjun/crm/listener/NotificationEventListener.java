package com.arjun.crm.listener;

import com.arjun.crm.entity.User;
import com.arjun.crm.enums.NotificationType;
import com.arjun.crm.enums.ReferenceType;
import com.arjun.crm.event.*;
import com.arjun.crm.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventListener {

    private final NotificationService notificationService;

    /**
     * Handle task assigned event
     */
    @Async
    @TransactionalEventListener
    public void handleTaskAssignedEvent(TaskAssignedEvent event) {
        log.info("Handling TaskAssignedEvent for task: {}", event.getTask().getId());

        if (event.getAssignedTo() != null) {
            String title = "Task Assigned";
            String message = String.format(
                    "%s assigned you to task: %s",
                    event.getAssignedBy().getFullName(),
                    event.getTask().getTitle()
            );

            notificationService.createNotification(
                    event.getAssignedTo(),
                    title,
                    message,
                    NotificationType.TASK_ASSIGNED,
                    event.getTask().getId(),
                    ReferenceType.TASK
            );
        }
    }

    /**
     * Handle task updated event
     */
    @Async
    @TransactionalEventListener
    public void handleTaskUpdatedEvent(TaskUpdatedEvent event) {
        log.info("Handling TaskUpdatedEvent for task: {}", event.getTask().getId());

        // Notify task assignee if different from updater
        if (event.getTask().getAssignedTo() != null &&
                !event.getTask().getAssignedTo().getId().equals(event.getUpdatedBy().getId())) {

            String title = "Task Updated";
            String message = String.format(
                    "%s updated task: %s - %s",
                    event.getUpdatedBy().getFullName(),
                    event.getTask().getTitle(),
                    event.getChangeDescription()
            );

            notificationService.createNotification(
                    event.getTask().getAssignedTo(),
                    title,
                    message,
                    NotificationType.TASK_UPDATED,
                    event.getTask().getId(),
                    ReferenceType.TASK
            );
        }

        // Notify task creator if different from updater and assignee
        if (event.getTask().getCreatedBy() != null &&
                !event.getTask().getCreatedBy().getId().equals(event.getUpdatedBy().getId()) &&
                (event.getTask().getAssignedTo() == null ||
                        !event.getTask().getCreatedBy().getId().equals(event.getTask().getAssignedTo().getId()))) {

            String title = "Task Updated";
            String message = String.format(
                    "%s updated your task: %s - %s",
                    event.getUpdatedBy().getFullName(),
                    event.getTask().getTitle(),
                    event.getChangeDescription()
            );

            notificationService.createNotification(
                    event.getTask().getCreatedBy(),
                    title,
                    message,
                    NotificationType.TASK_UPDATED,
                    event.getTask().getId(),
                    ReferenceType.TASK
            );
        }
    }

    /**
     * Handle comment added event
     */
    @Async
    @TransactionalEventListener
    public void handleCommentAddedEvent(CommentAddedEvent event) {
        log.info("Handling CommentAddedEvent for task: {}", event.getTask().getId());

        // Notify task assignee if different from comment author
        if (event.getTask().getAssignedTo() != null &&
                !event.getTask().getAssignedTo().getId().equals(event.getAuthor().getId())) {

            String title = "New Comment";
            String message = String.format(
                    "%s commented on task: %s",
                    event.getAuthor().getFullName(),
                    event.getTask().getTitle()
            );

            notificationService.createNotification(
                    event.getTask().getAssignedTo(),
                    title,
                    message,
                    NotificationType.COMMENT_ADDED,
                    event.getTask().getId(),
                    ReferenceType.COMMENT
            );
        }

        // Notify task creator if different from author and assignee
        if (event.getTask().getCreatedBy() != null &&
                !event.getTask().getCreatedBy().getId().equals(event.getAuthor().getId()) &&
                (event.getTask().getAssignedTo() == null ||
                        !event.getTask().getCreatedBy().getId().equals(event.getTask().getAssignedTo().getId()))) {

            String title = "New Comment";
            String message = String.format(
                    "%s commented on your task: %s",
                    event.getAuthor().getFullName(),
                    event.getTask().getTitle()
            );

            notificationService.createNotification(
                    event.getTask().getCreatedBy(),
                    title,
                    message,
                    NotificationType.COMMENT_ADDED,
                    event.getTask().getId(),
                    ReferenceType.COMMENT
            );
        }
    }

    /**
     * Handle mention created event
     */
    @Async
    @TransactionalEventListener
    public void handleMentionCreatedEvent(MentionCreatedEvent event) {
        log.info("Handling MentionCreatedEvent for comment: {}", event.getComment().getId());

        for (User mentionedUser : event.getMentionedUsers()) {
            // Don't notify if user mentioned themselves
            if (!mentionedUser.getId().equals(event.getAuthor().getId())) {
                String title = "You were mentioned";
                String message = String.format(
                        "%s mentioned you in a comment on task: %s",
                        event.getAuthor().getFullName(),
                        event.getComment().getTask().getTitle()
                );

                notificationService.createNotification(
                        mentionedUser,
                        title,
                        message,
                        NotificationType.MENTION,
                        event.getComment().getTask().getId(),
                        ReferenceType.COMMENT
                );
            }
        }
    }

    /**
     * Handle deadline reminder event
     */
    @Async
    @EventListener
    public void handleDeadlineReminderEvent(DeadlineReminderEvent event) {
        log.info("Handling DeadlineReminderEvent for task: {}", event.getTask().getId());

        if (event.getTask().getAssignedTo() != null) {
            String title = event.getReminderType().equals("OVERDUE") ?
                    "Task Overdue" : "Upcoming Deadline";
            
            String message = event.getReminderType().equals("OVERDUE") ?
                    String.format("Task '%s' is overdue! Due date was: %s",
                            event.getTask().getTitle(),
                            event.getTask().getDueDate()) :
                    String.format("Task '%s' is due soon: %s",
                            event.getTask().getTitle(),
                            event.getTask().getDueDate());

            notificationService.createNotification(
                    event.getTask().getAssignedTo(),
                    title,
                    message,
                    NotificationType.DEADLINE_REMINDER,
                    event.getTask().getId(),
                    ReferenceType.TASK
            );
        }
    }

    /**
     * Handle workspace invite event
     */
    @Async
    @TransactionalEventListener
    public void handleWorkspaceInviteEvent(WorkspaceInviteEvent event) {
        log.info("Handling WorkspaceInviteEvent for workspace: {}", event.getWorkspace().getId());

        String title = "Workspace Invitation";
        String message = String.format(
                "%s invited you to workspace: %s",
                event.getInvitedBy().getFullName(),
                event.getWorkspace().getName()
        );

        notificationService.createNotification(
                event.getInvitedUser(),
                title,
                message,
                NotificationType.WORKSPACE_INVITE,
                event.getWorkspace().getId(),
                ReferenceType.WORKSPACE
        );
    }

    /**
     * Handle project invite event
     */
    @Async
    @TransactionalEventListener
    public void handleProjectInviteEvent(ProjectInviteEvent event) {
        log.info("Handling ProjectInviteEvent for project: {}", event.getProject().getId());

        String title = "Project Invitation";
        String message = String.format(
                "%s invited you to project: %s",
                event.getInvitedBy().getFullName(),
                event.getProject().getName()
        );

        notificationService.createNotification(
                event.getInvitedUser(),
                title,
                message,
                NotificationType.PROJECT_INVITE,
                event.getProject().getId(),
                ReferenceType.PROJECT
        );
    }
}
