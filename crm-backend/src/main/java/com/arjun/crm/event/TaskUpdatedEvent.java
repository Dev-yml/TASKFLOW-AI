package com.arjun.crm.event;

import com.arjun.crm.entity.Task;
import com.arjun.crm.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TaskUpdatedEvent extends ApplicationEvent {
    
    private final Task task;
    private final User updatedBy;
    private final String changeDescription;

    public TaskUpdatedEvent(Object source, Task task, User updatedBy, String changeDescription) {
        super(source);
        this.task = task;
        this.updatedBy = updatedBy;
        this.changeDescription = changeDescription;
    }
}
