package com.arjun.crm.event;

import com.arjun.crm.entity.Task;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DeadlineReminderEvent extends ApplicationEvent {
    
    private final Task task;
    private final String reminderType; // "OVERDUE" or "UPCOMING"

    public DeadlineReminderEvent(Object source, Task task, String reminderType) {
        super(source);
        this.task = task;
        this.reminderType = reminderType;
    }
}
