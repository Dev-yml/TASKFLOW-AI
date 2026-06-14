package com.arjun.crm.dto.response;

import com.arjun.crm.entity.TaskActivity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskActivityResponse {
    private Long id;
    private Long taskId;
    private Long userId;
    private String userName;
    private String action;
    private String oldValue;
    private String newValue;
    private LocalDateTime createdAt;

    public static TaskActivityResponse fromEntity(TaskActivity activity) {
        return TaskActivityResponse.builder()
                .id(activity.getId())
                .taskId(activity.getTask().getId())
                .userId(activity.getUser().getId())
                .userName(activity.getUser().getFullName())
                .action(activity.getAction())
                .oldValue(activity.getOldValue())
                .newValue(activity.getNewValue())
                .createdAt(activity.getCreatedAt())
                .build();
    }
}
