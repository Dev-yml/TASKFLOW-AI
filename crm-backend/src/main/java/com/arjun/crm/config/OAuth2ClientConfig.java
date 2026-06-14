package com.arjun.crm.config;

import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientPropertiesMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Builds a ClientRegistrationRepository that only includes providers
 * whose client-id is actually configured (non-empty).
 *
 * This lets the application start even when GOOGLE_CLIENT_ID /
 * GITHUB_CLIENT_ID env vars are not yet set.
 */
@Configuration
public class OAuth2ClientConfig {

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(
            OAuth2ClientProperties properties) {

        List<ClientRegistration> registrations = new OAuth2ClientPropertiesMapper(properties)
                .asClientRegistrations()
                .entrySet()
                .stream()
                .filter(e -> {
                    // Keep only registrations with a real (non-placeholder) client-id
                    String id = e.getValue().getClientId();
                    return id != null
                            && !id.isBlank()
                            && !id.startsWith("your_")
                            && !id.equals("placeholder");
                })
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

        if (registrations.isEmpty()) {
            // Return a minimal repository so Spring doesn't crash
            // OAuth2 buttons will be shown but will 404 until credentials are set
            return registrationId -> null;
        }

        return new InMemoryClientRegistrationRepository(registrations);
    }
}
