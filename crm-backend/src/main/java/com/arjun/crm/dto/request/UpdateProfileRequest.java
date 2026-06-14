package com.arjun.crm.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProfileRequest {

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    // Extended profile fields — all optional
    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Size(max = 80)
    private String displayName;

    @Size(max = 500)
    private String bio;

    @Size(max = 100)
    private String designation;

    @Size(max = 100)
    private String department;

    @Size(max = 30)
    private String phoneNumber;

    @Size(max = 100)
    private String location;

    @Size(max = 255)
    private String website;

    @Size(max = 255)
    private String linkedinUrl;

    @Size(max = 255)
    private String githubUrl;

    @Size(max = 50)
    private String timezone;

    @Size(max = 30)
    private String preferredLanguage;
}
