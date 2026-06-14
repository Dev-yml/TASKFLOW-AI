package com.arjun.crm.event;

import com.arjun.crm.entity.User;
import com.arjun.crm.entity.Workspace;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class WorkspaceInviteEvent extends ApplicationEvent {
    
    private final Workspace workspace;
    private final User invitedUser;
    private final User invitedBy;

    public WorkspaceInviteEvent(Object source, Workspace workspace, User invitedUser, User invitedBy) {
        super(source);
        this.workspace = workspace;
        this.invitedUser = invitedUser;
        this.invitedBy = invitedBy;
    }
}
