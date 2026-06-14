package com.arjun.crm.entity;

import com.arjun.crm.enums.Role;
import com.arjun.crm.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_user_email", columnList = "email", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;

    // ── Extended profile fields ──────────────────────────────────────────
    @Column(length = 50)
    private String firstName;

    @Column(length = 50)
    private String lastName;

    @Column(length = 80)
    private String displayName;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(length = 100)
    private String designation;

    @Column(length = 100)
    private String department;

    @Column(length = 30)
    private String phoneNumber;

    @Column(length = 100)
    private String location;

    @Column(length = 255)
    private String website;

    @Column(length = 255)
    private String linkedinUrl;

    @Column(length = 255)
    private String githubUrl;

    @Column(columnDefinition = "TEXT")
    private String profileImageUrl;

    @Column(length = 50)
    private String timezone;

    @Column(length = 30)
    private String preferredLanguage;

    // ── OAuth2 provider fields ───────────────────────────────────────────
    /** "local" | "google" | "github" */
    @Column(length = 20)
    @Builder.Default
    private String provider = "local";

    /** The subject/id returned by the OAuth2 provider */
    @Column(length = 100)
    private String providerId;
    // ────────────────────────────────────────────────────────────────────

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
