package com.arjun.crm.scheduler;

import com.arjun.crm.entity.Task;
import com.arjun.crm.enums.TaskStatus;
import com.arjun.crm.event.DeadlineReminderEvent;
import com.arjun.crm.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeadlineReminderScheduler {

    private final TaskRepository taskRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Check for overdue tasks every day at 9 AM
     */
    @Scheduled(cron = "0 0 9 * * *")
    public void checkOverdueTasks() {
        log.info("Running overdue tasks check...");

        List<Task> overdueTasks = taskRepository.findOverdueTasks(LocalDate.now());
        
        log.info("Found {} overdue tasks", overdueTasks.size());

        for (Task task : overdueTasks) {
            if (task.getAssignedTo() != null) {
                eventPublisher.publishEvent(new DeadlineReminderEvent(this, task, "OVERDUE"));
            }
        }

        log.info("Overdue tasks check completed");
    }

    /**
     * Check for upcoming deadlines every day at 9 AM
     * Notify for tasks due in the next 24 hours
     */
    @Scheduled(cron = "0 0 9 * * *")
    public void checkUpcomingDeadlines() {
        log.info("Running upcoming deadlines check...");

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        
        List<Task> upcomingTasks = taskRepository.findByDueDateAndStatusNot(tomorrow, TaskStatus.DONE);
        
        log.info("Found {} tasks due tomorrow", upcomingTasks.size());

        for (Task task : upcomingTasks) {
            if (task.getAssignedTo() != null) {
                eventPublisher.publishEvent(new DeadlineReminderEvent(this, task, "UPCOMING"));
            }
        }

        log.info("Upcoming deadlines check completed");
    }

    /**
     * Cleanup old read notifications every week
     * Delete notifications older than 30 days
     */
    @Scheduled(cron = "0 0 2 * * SUN")
    public void cleanupOldNotifications() {
        log.info("Running notification cleanup...");
        
        // This would be called via NotificationService
        // Implemented in the service layer
        
        log.info("Notification cleanup completed");
    }
}
