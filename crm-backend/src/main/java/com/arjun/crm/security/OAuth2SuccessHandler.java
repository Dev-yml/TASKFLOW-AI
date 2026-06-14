package com.arjun.crm.security;

import com.arjun.crm.dto.response.UserResponse;
import com.arjun.crm.entity.User;
import com.arjun.crm.enums.Role;
import com.arjun.crm.enums.UserStatus;
import com.arjun.crm.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Handles successful OAuth2 login.
 *
 * Flow:
 *  OAuth2 provider → Spring Security → this handler
 *  → find or create User in DB
 *  → generate JWT
 *  → redirect to frontend /oauth2/callback?token=JWT
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Value("${oauth2.redirect-uri:http://localhost:3000/oauth2/callback}")
    private String redirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        String provider = oauthToken.getAuthorizedClientRegistrationId(); // "google" or "github"
        OAuth2User oauthUser = oauthToken.getPrincipal();

        log.info("OAuth2 login success via provider: {}", provider);

        User user = findOrCreateUser(provider, oauthUser);

        // Generate JWT
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String jwt = jwtService.generateToken(Map.of("userId", user.getId()), userDetails);

        // Redirect to frontend with token
        String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("token", jwt)
                .build().toUriString();

        log.info("Redirecting OAuth2 user {} to {}", user.getEmail(), redirectUri);

        // Invalidate the Spring session so only the JWT is used for subsequent requests.
        // This prevents a stale session cookie from interfering with JWT-based API calls.
        try {
            request.getSession(false);
            if (request.getSession(false) != null) {
                request.getSession(false).invalidate();
            }
        } catch (Exception ignored) { }

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private User findOrCreateUser(String provider, OAuth2User oauthUser) {
        Map<String, Object> attrs = oauthUser.getAttributes();

        String providerId;
        String email;
        String name;
        String picture;

        if ("google".equals(provider)) {
            providerId = String.valueOf(attrs.get("sub"));
            email      = (String) attrs.get("email");
            name       = (String) attrs.getOrDefault("name", email);
            picture    = (String) attrs.get("picture");
        } else {
            // GitHub — id is Integer
            providerId = String.valueOf(attrs.get("id"));
            email      = (String) attrs.get("email");
            name       = (String) attrs.getOrDefault("name",
                             attrs.getOrDefault("login", "GitHub User"));
            picture    = (String) attrs.get("avatar_url");
            // GitHub may return null email if user keeps it private
            if (email == null || email.isBlank()) {
                email = providerId + "@github.oauth";
            }
        }

        final String finalEmail   = email;
        final String finalName    = name;
        final String finalPicture = picture;

        // 1. Try to find by provider + providerId (safest lookup)
        Optional<User> byProvider = userRepository.findByProviderAndProviderId(provider, providerId);
        if (byProvider.isPresent()) {
            User existing = byProvider.get();
            // Refresh picture if changed
            if (finalPicture != null) existing.setProfileImageUrl(finalPicture);
            return userRepository.save(existing);
        }

        // 2. Try to find by email (existing local account — link it)
        Optional<User> byEmail = userRepository.findByEmail(finalEmail);
        if (byEmail.isPresent()) {
            User existing = byEmail.get();
            existing.setProvider(provider);
            existing.setProviderId(providerId);
            if (finalPicture != null && existing.getProfileImageUrl() == null) {
                existing.setProfileImageUrl(finalPicture);
            }
            return userRepository.save(existing);
        }

        // 3. Create brand-new user
        User newUser = User.builder()
                .fullName(finalName)
                .email(finalEmail)
                .password("{oauth2}" + UUID.randomUUID()) // non-null, not usable for login
                .role(Role.USER)
                .status(UserStatus.ACTIVE)
                .provider(provider)
                .providerId(providerId)
                .profileImageUrl(finalPicture)
                .build();

        User saved = userRepository.save(newUser);
        log.info("Created new OAuth2 user: {} via {}", finalEmail, provider);
        return saved;
    }
}
