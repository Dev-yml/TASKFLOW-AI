package com.arjun.crm.service.impl;

import com.arjun.crm.dto.response.ActivityAnalyticsResponse;
import com.arjun.crm.dto.response.TaskAnalyticsResponse;
import com.arjun.crm.dto.response.TeamPerformanceResponse;
import com.arjun.crm.enums.TaskPriority;
import com.arjun.crm.enums.TaskStatus;
import com.arjun.crm.repository.*;
import com.arjun.crm.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arjun.crm.entity.Task;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsServiceImpl implements AnalyticsService {

    private final TaskRepository taskRepository;
    private final TaskActivityRepository taskActivityRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final TaskCommentRepository taskCommentRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "taskAnalytics", key = "#startDate + '_' + #endDate")
    public TaskAnalyticsResponse getTaskAnalytics(LocalDate startDate, LocalDate endDate) {
        log.info("Fetching task analytics from {} to {}", startDate, endDate);

        // Convert to LocalDateTime for query compatibility
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        // Basic statistics
        Long totalTasks = taskRepository.countTasksCreatedBetween(startDateTime, endDateTime);
        Long completedTasks = taskRepository.countTasksCompletedBetween(startDateTime, endDateTime);
        Long overdueTasks = (long) taskRepository.findOverdueTasks(LocalDate.now()).size();
        Long inProgressTasks = taskRepository.countByStatus(TaskStatus.IN_PROGRESS);
        
        Double completionRate = totalTasks > 0 ? (completedTasks * 100.0 / totalTasks) : 0.0;
        
        // Average completion time
        double averageCompletionDays = calculateAverageCompletionDays(startDateTime, endDateTime);

        // Tasks by status
        Map<TaskStatus, Long> tasksByStatus = new HashMap<>();
        for (TaskStatus status : TaskStatus.values()) {
            tasksByStatus.put(status, taskRepository.countByStatus(status));
        }

        // Tasks by priority
        Map<TaskPriority, Long> tasksByPriority = new HashMap<>();
        for (TaskPriority priority : TaskPriority.values()) {
            Long count = taskRepository.countByPriority(priority);
            tasksByPriority.put(priority, count);
        }

        // Tasks by project (top 10)
        Map<String, Long> tasksByProject = new LinkedHashMap<>();
        List<Object[]> projectStats = taskRepository.countTasksByProject(startDateTime, endDateTime);
        projectStats.stream()
                .limit(10)
                .forEach(row -> {
                    String projectName = row[1] != null ? row[1].toString() : "No Project";
                    Long count = row[2] != null ? ((Number) row[2]).longValue() : 0L;
                    tasksByProject.put(projectName, count);
                });

        // Tasks by user (top 10)
        Map<String, Long> tasksByUser = new LinkedHashMap<>();
        List<Object[]> userStats = taskRepository.countTasksByUser(startDateTime, endDateTime);
        userStats.stream()
                .limit(10)
                .forEach(row -> {
                    String userName = row[1] != null ? row[1].toString() : "Unknown User";
                    Long count = row[2] != null ? ((Number) row[2]).longValue() : 0L;
                    tasksByUser.put(userName, count);
                });

        return TaskAnalyticsResponse.builder()
                .totalTasks(totalTasks)
                .completedTasks(completedTasks)
                .overdueTasks(overdueTasks)
                .inProgressTasks(inProgressTasks)
                .completionRate(Math.round(completionRate * 100.0) / 100.0)
                .averageCompletionDays(Math.round(averageCompletionDays * 100.0) / 100.0)
                .tasksByStatus(tasksByStatus)
                .tasksByPriority(tasksByPriority)
                .tasksByProject(tasksByProject)
                .tasksByUser(tasksByUser)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "teamPerformance", key = "#startDate + '_' + #endDate")
    public TeamPerformanceResponse getTeamPerformance(LocalDate startDate, LocalDate endDate) {
        log.info("Fetching team performance from {} to {}", startDate, endDate);

        // Get all users and their performance
        List<TeamPerformanceResponse.UserPerformance> userPerformances = new ArrayList<>();
        
        // This would require custom queries to aggregate user data
        // Simplified example:
        userRepository.findAll().forEach(user -> {
            Long tasksCompleted = taskRepository.countByCreatedByIdAndStatus(user.getId(), TaskStatus.DONE);
            Long commentsPosted = taskCommentRepository.countByUserId(user.getId());
            Long messagesSet = chatMessageRepository.countBySenderId(user.getId());
            Double activityScore = tasksCompleted * 3.0 + commentsPosted * 2.0 + messagesSet * 1.0;

            userPerformances.add(TeamPerformanceResponse.UserPerformance.builder()
                    .userId(user.getId())
                    .userName(user.getFullName())
                    .userEmail(user.getEmail())
                    .tasksCompleted(tasksCompleted)
                    .commentsPosted(commentsPosted)
                    .messagesSet(messagesSet)
                    .activityScore(activityScore)
                    .build());
        });

        // Sort by activity score and assign ranks
        userPerformances.sort((a, b) -> Double.compare(b.getActivityScore(), a.getActivityScore()));
        for (int i = 0; i < userPerformances.size(); i++) {
            userPerformances.get(i).setRank(i + 1);
        }

        // Get top 10 performers
        List<TeamPerformanceResponse.UserPerformance> topPerformers = userPerformances.stream()
                .limit(10)
                .collect(Collectors.toList());

        // Calculate averages
        Double averageTasksPerUser = userPerformances.stream()
                .mapToLong(TeamPerformanceResponse.UserPerformance::getTasksCompleted)
                .average()
                .orElse(0.0);

        Long totalTeamTasks = userPerformances.stream()
                .mapToLong(TeamPerformanceResponse.UserPerformance::getTasksCompleted)
                .sum();

        Long totalTeamMessages = userPerformances.stream()
                .mapToLong(TeamPerformanceResponse.UserPerformance::getMessagesSet)
                .sum();

        return TeamPerformanceResponse.builder()
                .topPerformers(topPerformers)
                .averageTasksPerUser(Math.round(averageTasksPerUser * 100.0) / 100.0)
                .averageResponseTime(2.5) // Placeholder
                .totalTeamTasks(totalTeamTasks)
                .totalTeamMessages(totalTeamMessages)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "activityAnalytics", key = "#startDate + '_' + #endDate")
    public ActivityAnalyticsResponse getActivityAnalytics(LocalDate startDate, LocalDate endDate) {
        log.info("Fetching activity analytics from {} to {}", startDate, endDate);

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        Long totalActivities = taskActivityRepository.count();

        // Activities by type
        Map<String, Long> activitiesByType = new LinkedHashMap<>();
        List<Object[]> typeStats = taskActivityRepository.countActivitiesByType(startDateTime, endDateTime);
        typeStats.forEach(row -> {
            String type = (String) row[0];
            Long count = ((Number) row[1]).longValue();
            activitiesByType.put(type, count);
        });

        // Activities by date
        Map<LocalDate, Long> activitiesByDate = new LinkedHashMap<>();
        List<Object[]> dateStats = taskActivityRepository.countActivitiesByDate(startDateTime, endDateTime);
        dateStats.forEach(row -> {
            LocalDate date;
            if (row[0] instanceof java.sql.Date) {
                date = ((java.sql.Date) row[0]).toLocalDate();
            } else if (row[0] instanceof LocalDate) {
                date = (LocalDate) row[0];
            } else {
                date = LocalDate.parse(row[0].toString());
            }
            Long count = ((Number) row[1]).longValue();
            activitiesByDate.put(date, count);
        });

        // Most active users (top 10)
        List<ActivityAnalyticsResponse.MostActiveUser> mostActiveUsers = new ArrayList<>();
        List<Object[]> userStats = taskActivityRepository.findMostActiveUsers(
                startDateTime, endDateTime, 
                org.springframework.data.domain.PageRequest.of(0, 10));
        userStats.forEach(row -> {
            Long userId = ((Number) row[0]).longValue();
            String userName = (String) row[1];
            Long activityCount = ((Number) row[2]).longValue();
            mostActiveUsers.add(ActivityAnalyticsResponse.MostActiveUser.builder()
                    .userId(userId)
                    .userName(userName)
                    .activityCount(activityCount)
                    .build());
        });

        // Most active projects (top 10)
        List<ActivityAnalyticsResponse.MostActiveProject> mostActiveProjects = new ArrayList<>();
        List<Object[]> projectStats = taskActivityRepository.findMostActiveProjects(
                startDateTime, endDateTime,
                org.springframework.data.domain.PageRequest.of(0, 10));
        projectStats.forEach(row -> {
            Long projectId = ((Number) row[0]).longValue();
            String projectName = (String) row[1];
            Long activityCount = ((Number) row[2]).longValue();
            mostActiveProjects.add(ActivityAnalyticsResponse.MostActiveProject.builder()
                    .projectId(projectId)
                    .projectName(projectName)
                    .activityCount(activityCount)
                    .build());
        });

        return ActivityAnalyticsResponse.builder()
                .totalActivities(totalActivities)
                .activitiesByType(activitiesByType)
                .activitiesByDate(activitiesByDate)
                .mostActiveUsers(mostActiveUsers)
                .mostActiveProjects(mostActiveProjects)
                .build();
    }

    private double calculateAverageCompletionDays(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<Task> completedTasks = taskRepository.findByStatusAndCreatedAtBetween(TaskStatus.DONE, startDateTime, endDateTime);
        return completedTasks.stream()
                .filter(task -> task.getCreatedAt() != null && task.getUpdatedAt() != null)
                .mapToLong(task -> ChronoUnit.DAYS.between(task.getCreatedAt(), task.getUpdatedAt()))
                .average()
                .orElse(0.0);
    }
}
