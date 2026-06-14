package com.arjun.crm.dto.response;

import com.arjun.crm.entity.TaskWatcher;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskWatcherResponse {
    private Long id;
    private Long taskId;
    private Long userId;
    private String userName;
    private String userEmail;
    private LocalDateTime watchedAt;

    public static TaskWatcherResponse fromEntity(TaskWatcher watcher) {
        return TaskWatcherResponse.builder()
                .id(watcher.getId())
                .taskId(watcher.getTask().getId())
                .userId(watcher.getUser().getId())
                .userName(watcher.getUser().getFullName())
                .userEmail(watcher.getUser().getEmail())
                .watchedAt(watcher.getWatchedAt())
                .build();
    }
}
