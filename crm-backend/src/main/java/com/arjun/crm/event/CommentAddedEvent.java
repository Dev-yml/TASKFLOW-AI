package com.arjun.crm.event;

import com.arjun.crm.entity.Task;
import com.arjun.crm.entity.TaskComment;
import com.arjun.crm.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CommentAddedEvent extends ApplicationEvent {
    
    private final TaskComment comment;
    private final Task task;
    private final User author;

    public CommentAddedEvent(Object source, TaskComment comment, Task task, User author) {
        super(source);
        this.comment = comment;
        this.task = task;
        this.author = author;
    }
}
