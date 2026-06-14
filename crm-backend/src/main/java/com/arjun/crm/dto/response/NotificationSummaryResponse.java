package com.arjun.crm.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationSummaryResponse {
    
    private Long unreadCount;
    private Long totalCount;
}
