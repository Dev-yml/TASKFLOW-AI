package com.arjun.crm.dto.request;

import com.arjun.crm.enums.WorkspaceRole;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddWorkspaceMemberRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Role is required")
    private WorkspaceRole role;
}
