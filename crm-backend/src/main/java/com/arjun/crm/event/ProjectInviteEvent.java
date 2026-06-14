package com.arjun.crm.event;

import com.arjun.crm.entity.Project;
import com.arjun.crm.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ProjectInviteEvent extends ApplicationEvent {
    
    private final Project project;
    private final User invitedUser;
    private final User invitedBy;

    public ProjectInviteEvent(Object source, Project project, User invitedUser, User invitedBy) {
        super(source);
        this.project = project;
        this.invitedUser = invitedUser;
        this.invitedBy = invitedBy;
    }
}
