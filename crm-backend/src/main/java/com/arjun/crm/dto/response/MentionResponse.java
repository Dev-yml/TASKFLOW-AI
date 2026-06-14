package com.arjun.crm.dto.response;

import com.arjun.crm.entity.Mention;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentionResponse {
    private Long id;
    private Long userId;
    private String userName;
    private String userEmail;
    private LocalDateTime createdAt;

    public static MentionResponse fromEntity(Mention mention) {
        return MentionResponse.builder()
                .id(mention.getId())
                .userId(mention.getMentionedUser().getId())
                .userName(mention.getMentionedUser().getFullName())
                .userEmail(mention.getMentionedUser().getEmail())
                .createdAt(mention.getCreatedAt())
                .build();
    }
}
