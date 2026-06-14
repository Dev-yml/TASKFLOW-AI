package com.arjun.crm.ai.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * AI Configuration
 * Configures AI services and REST client
 * 
 * Features:
 * - RestTemplate for AI API calls
 * - Retry mechanism for failed requests
 * - Timeout configuration
 * - Connection pooling
 * 
 * @author CRM Backend Team
 * @version 1.0
 */
@Configuration
@EnableRetry
public class AIConfig {

    /**
     * REST Template for AI API calls
     * Configured with timeouts and error handling
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofSeconds(10))
                .setReadTimeout(Duration.ofSeconds(30))
                .build();
    }
}
