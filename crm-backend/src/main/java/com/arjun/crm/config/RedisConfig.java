package com.arjun.crm.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Redis Configuration
 * Configures Redis for caching and data storage
 * 
 * Features:
 * - RedisTemplate with JSON serialization
 * - Cache manager with custom TTLs
 * - Multiple cache configurations
 * - String and JSON serializers
 * 
 * @author CRM Backend Team
 * @version 1.0
 */
@Configuration
@EnableCaching
public class RedisConfig {

    @Value("${cache.ttl.dashboard:600000}")
    private long dashboardTtl;

    @Value("${cache.ttl.analytics:600000}")
    private long analyticsTtl;

    @Value("${cache.ttl.ai-responses:1800000}")
    private long aiResponsesTtl;

    @Value("${cache.ttl.user-profile:3600000}")
    private long userProfileTtl;

    @Value("${cache.ttl.notifications:300000}")
    private long notificationsTtl;

    /**
     * ObjectMapper specifically configured for Redis serialization.
     * Must support LocalDate/LocalDateTime as Map keys and values.
     */
    @Bean
    public ObjectMapper redisObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // Java 8 date/time support (values AND map keys)
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        mapper.registerModule(javaTimeModule);

        // Write dates as ISO strings, not timestamps
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // Write date map keys as strings (ISO-8601), not timestamps
        mapper.disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);
        // Don't fail on unknown properties when deserializing
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        // Enable polymorphic type handling so Redis can reconstruct the correct type
        mapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );

        return mapper;
    }

    /**
     * Redis Template Configuration
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory,
                                                        @Qualifier("redisObjectMapper") ObjectMapper redisObjectMapper) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // Use String serializer for keys
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);

        // Use our properly configured JSON serializer for values
        GenericJackson2JsonRedisSerializer jsonSerializer =
                new GenericJackson2JsonRedisSerializer(redisObjectMapper);
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);

        template.afterPropertiesSet();
        return template;
    }

    /**
     * Cache Manager Configuration
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory,
                                      @Qualifier("redisObjectMapper") ObjectMapper redisObjectMapper) {
        GenericJackson2JsonRedisSerializer jsonSerializer =
                new GenericJackson2JsonRedisSerializer(redisObjectMapper);

        // Default cache configuration (10 minutes)
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMillis(600000))
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer))
                .disableCachingNullValues();

        // Custom cache configurations with specific TTLs
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        // Dashboard cache (10 minutes)
        cacheConfigurations.put("dashboard", createCacheConfig(dashboardTtl, jsonSerializer));
        cacheConfigurations.put("dashboardOverview", createCacheConfig(dashboardTtl, jsonSerializer));

        // Analytics cache (10 minutes)
        cacheConfigurations.put("taskAnalytics", createCacheConfig(analyticsTtl, jsonSerializer));
        cacheConfigurations.put("teamPerformance", createCacheConfig(analyticsTtl, jsonSerializer));
        cacheConfigurations.put("activityAnalytics", createCacheConfig(analyticsTtl, jsonSerializer));

        // AI responses cache (30 minutes)
        cacheConfigurations.put("aiResponses", createCacheConfig(aiResponsesTtl, jsonSerializer));
        cacheConfigurations.put("aiTaskPrioritization", createCacheConfig(aiResponsesTtl, jsonSerializer));
        cacheConfigurations.put("aiDeadlinePrediction", createCacheConfig(aiResponsesTtl, jsonSerializer));

        // User profile cache (1 hour)
        cacheConfigurations.put("userProfile", createCacheConfig(userProfileTtl, jsonSerializer));

        // Notifications cache (5 minutes)
        cacheConfigurations.put("notifications", createCacheConfig(notificationsTtl, jsonSerializer));

        // Reports cache (10 minutes)
        cacheConfigurations.put("dailyReport", createCacheConfig(analyticsTtl, jsonSerializer));
        cacheConfigurations.put("weeklyReport", createCacheConfig(analyticsTtl, jsonSerializer));
        cacheConfigurations.put("monthlyReport", createCacheConfig(analyticsTtl, jsonSerializer));

        return RedisCacheManager.builder(factory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .transactionAware()
                .build();
    }

    /**
     * Create cache configuration with custom TTL and serializer
     */
    private RedisCacheConfiguration createCacheConfig(long ttlMillis,
                                                        GenericJackson2JsonRedisSerializer jsonSerializer) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMillis(ttlMillis))
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer))
                .disableCachingNullValues();
    }
}
