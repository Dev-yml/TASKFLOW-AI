package com.arjun.crm.event;

import com.arjun.crm.entity.TaskComment;
import com.arjun.crm.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class MentionCreatedEvent extends ApplicationEvent {
    
    private final TaskComment comment;
    private final List<User> mentionedUsers;
    private final User author;

    public MentionCreatedEvent(Object source, TaskComment comment, List<User> mentionedUsers, User author) {
        super(source);
        this.comment = comment;
        this.mentionedUsers = mentionedUsers;
        this.author = author;
    }
}
