package com.arjun.crm.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Receives the Cloudinary secure URL after the frontend uploads
 * the image directly to Cloudinary (unsigned upload preset).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AvatarUploadRequest {

    @NotBlank(message = "Avatar URL is required")
    private String avatarUrl;
}
