package com.arjun.crm.dto.response;

import com.arjun.crm.entity.User;
import com.arjun.crm.enums.Role;
import com.arjun.crm.enums.UserStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    // core (never removed)
    private Long id;
    private String fullName;
    private String email;
    private Role role;
    private UserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // extended profile
    private String firstName;
    private String lastName;
    private String displayName;
    private String bio;
    private String designation;
    private String department;
    private String phoneNumber;
    private String location;
    private String website;
    private String linkedinUrl;
    private String githubUrl;
    private String profileImageUrl;
    private String timezone;
    private String preferredLanguage;

    public static UserResponse fromEntity(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                // extended
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .displayName(user.getDisplayName())
                .bio(user.getBio())
                .designation(user.getDesignation())
                .department(user.getDepartment())
                .phoneNumber(user.getPhoneNumber())
                .location(user.getLocation())
                .website(user.getWebsite())
                .linkedinUrl(user.getLinkedinUrl())
                .githubUrl(user.getGithubUrl())
                .profileImageUrl(user.getProfileImageUrl())
                .timezone(user.getTimezone())
                .preferredLanguage(user.getPreferredLanguage())
                .build();
    }
}
