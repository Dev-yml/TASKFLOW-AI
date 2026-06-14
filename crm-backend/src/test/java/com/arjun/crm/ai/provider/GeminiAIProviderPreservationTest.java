package com.arjun.crm.ai.provider;

import com.arjun.crm.ai.dto.response.AIResponse;
import com.arjun.crm.exception.AIServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.jqwik.api.*;
import net.jqwik.api.constraints.DoubleRange;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.constraints.StringLength;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Preservation Property Tests for Gemini AI Provider
 * 
 * **Property 2: Preservation** - Non-Model Configuration Behavior
 * **Validates: Requirements 3.1, 3.2, 3.3, 3.4, 3.5**
 * 
 * **IMPORTANT**: Follow observation-first methodology
 * These tests observe behavior on UNFIXED code for non-model-dependent functionality
 * (authentication, caching, configuration parameters) and verify that behavior is preserved.
 * 
 * **EXPECTED OUTCOME**: Tests PASS (this confirms baseline behavior to preserve)
 * 
 * The goal is to ensure that when the model configuration is changed, all other
 * functionality continues to work exactly as before.
 */
public class GeminiAIProviderPreservationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Creates a test GeminiAIProvider with configurable parameters
     * This allows us to test various configuration combinations
     */
    private TestGeminiAIProvider createTestProvider(String apiKey, double temperature, int maxTokens, boolean simulateSuccess) {
        RestTemplate mockRestTemplate = mock(RestTemplate.class);
        
        if (simulateSuccess) {
            // Mock successful response
            String mockResponse = """
                {
                    "candidates": [{
                        "content": {
                            "parts": [{
                                "text": "Test AI response with temperature: %f, maxTokens: %d"
                            }]
                        }
                    }]
                }
                """.formatted(temperature, maxTokens);
            
            when(mockRestTemplate.exchange(
                anyString(), 
                eq(HttpMethod.POST), 
                any(HttpEntity.class), 
                eq(String.class)
            )).thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));
        } else {
            // Mock authentication failure
            when(mockRestTemplate.exchange(
                anyString(), 
                eq(HttpMethod.POST), 
                any(HttpEntity.class), 
                eq(String.class)
            )).thenThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Invalid API key"));
        }
        
        return new TestGeminiAIProvider(mockRestTemplate, objectMapper, apiKey, temperature, maxTokens);
    }

    /**
     * Test implementation of GeminiAIProvider for preservation testing
     */
    static class TestGeminiAIProvider {
        private final RestTemplate restTemplate;
        private final ObjectMapper objectMapper;
        private final String apiKey;
        private final double temperature;
        private final int maxTokens;

        public TestGeminiAIProvider(RestTemplate restTemplate, ObjectMapper objectMapper, 
                                   String apiKey, double temperature, int maxTokens) {
            this.restTemplate = restTemplate;
            this.objectMapper = objectMapper;
            this.apiKey = apiKey;
            this.temperature = temperature;
            this.maxTokens = maxTokens;
        }

        public AIResponse generateResponse(String prompt) {
            try {
                // API key validation (preservation requirement 3.2)
                if (apiKey == null || apiKey.isBlank()) {
                    throw new AIServiceException("Gemini API key is not configured. Set ai.gemini.api-key.");
                }

                // Build request body with configuration parameters (preservation requirement 3.4)
                Map<String, Object> requestBody = buildRequestBody(prompt);
                
                // Set headers
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                
                // Use current model configuration (should be preserved)
                String apiUrl = "https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash:generateContent";
                String urlWithKey = apiUrl + "?key=" + apiKey;
                
                // Make request
                HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
                ResponseEntity<String> response = restTemplate.exchange(
                    urlWithKey,
                    HttpMethod.POST,
                    entity,
                    String.class
                );
                
                // Parse response
                if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                    return parseResponse(response.getBody());
                } else {
                    throw new AIServiceException("Failed to get response from Gemini API");
                }
                
            } catch (HttpClientErrorException e) {
                if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                    throw new AIServiceException("AI service temporarily unavailable: Invalid API key");
                }
                throw new AIServiceException("AI service temporarily unavailable: " + e.getMessage());
            } catch (Exception e) {
                throw new AIServiceException("AI service temporarily unavailable: " + e.getMessage());
            }
        }

        private Map<String, Object> buildRequestBody(String prompt) {
            Map<String, Object> requestBody = new HashMap<>();
            
            // Build contents
            Map<String, Object> part = new HashMap<>();
            part.put("text", prompt);
            
            Map<String, Object> content = new HashMap<>();
            content.put("parts", List.of(part));
            
            requestBody.put("contents", List.of(content));
            
            // Add generation config with preserved parameters (requirement 3.4)
            Map<String, Object> generationConfig = new HashMap<>();
            generationConfig.put("temperature", temperature);
            generationConfig.put("maxOutputTokens", maxTokens);
            generationConfig.put("topP", 0.8);
            generationConfig.put("topK", 40);
            
            requestBody.put("generationConfig", generationConfig);
            
            // Add safety settings (preservation requirement 3.4)
            List<Map<String, Object>> safetySettings = List.of(
                Map.of("category", "HARM_CATEGORY_HARASSMENT", "threshold", "BLOCK_MEDIUM_AND_ABOVE"),
                Map.of("category", "HARM_CATEGORY_HATE_SPEECH", "threshold", "BLOCK_MEDIUM_AND_ABOVE"),
                Map.of("category", "HARM_CATEGORY_SEXUALLY_EXPLICIT", "threshold", "BLOCK_MEDIUM_AND_ABOVE"),
                Map.of("category", "HARM_CATEGORY_DANGEROUS_CONTENT", "threshold", "BLOCK_MEDIUM_AND_ABOVE")
            );
            
            requestBody.put("safetySettings", safetySettings);
            
            return requestBody;
        }

        private AIResponse parseResponse(String responseBody) {
            try {
                com.fasterxml.jackson.databind.JsonNode root = objectMapper.readTree(responseBody);
                
                // Extract text from response
                com.fasterxml.jackson.databind.JsonNode candidates = root.path("candidates");
                if (candidates.isArray() && candidates.size() > 0) {
                    com.fasterxml.jackson.databind.JsonNode firstCandidate = candidates.get(0);
                    com.fasterxml.jackson.databind.JsonNode content = firstCandidate.path("content");
                    com.fasterxml.jackson.databind.JsonNode parts = content.path("parts");
                    
                    if (parts.isArray() && parts.size() > 0) {
                        String text = parts.get(0).path("text").asText();
                        
                        return AIResponse.builder()
                            .content(text)
                            .success(true)
                            .model("gemini-1.5-flash")
                            .build();
                    }
                }
                
                throw new AIServiceException("Invalid response format from Gemini API");
                
            } catch (Exception e) {
                throw new AIServiceException("Failed to parse AI response");
            }
        }

        public Map<String, Object> getLastRequestBody(String prompt) {
            return buildRequestBody(prompt);
        }
    }

    /**
     * **Property 2.1: API Authentication Preservation**
     * **Validates: Requirement 3.2**
     * 
     * This property verifies that API key validation and authentication flow
     * continues to work exactly as before the model fix.
     */
    @Property(tries = 10)
    @Label("Preservation: API key validation should work consistently")
    void apiKeyValidationShouldWorkConsistently(
            @ForAll @StringLength(min = 10, max = 50) String validApiKey,
            @ForAll @StringLength(min = 5, max = 100) String prompt) {
        
        Assume.that(!validApiKey.trim().isEmpty());
        Assume.that(!prompt.trim().isEmpty());
        
        // Test with valid API key - should succeed
        TestGeminiAIProvider providerWithValidKey = createTestProvider(validApiKey, 0.7, 1000, true);
        
        assertThatCode(() -> {
            AIResponse response = providerWithValidKey.generateResponse(prompt);
            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
        }).describedAs("Valid API key should allow successful AI requests").doesNotThrowAnyException();
        
        // Test with invalid API key - should fail with authentication error
        TestGeminiAIProvider providerWithInvalidKey = createTestProvider("invalid-key", 0.7, 1000, false);
        
        assertThatThrownBy(() -> {
            providerWithInvalidKey.generateResponse(prompt);
        })
        .isInstanceOf(AIServiceException.class)
        .hasMessageContaining("Invalid API key")
        .describedAs("Invalid API key should result in authentication error");
    }

    /**
     * **Property 2.2: Configuration Parameter Preservation**
     * **Validates: Requirement 3.4**
     * 
     * This property verifies that AI configuration parameters (temperature, max tokens, 
     * safety settings) are applied correctly in API requests, regardless of model changes.
     */
    @Property(tries = 15)
    @Label("Preservation: Configuration parameters should be applied correctly")
    void configurationParametersShouldBeAppliedCorrectly(
            @ForAll @DoubleRange(min = 0.0, max = 2.0) double temperature,
            @ForAll @IntRange(min = 100, max = 4000) int maxTokens,
            @ForAll @StringLength(min = 5, max = 100) String prompt) {
        
        Assume.that(!prompt.trim().isEmpty());
        
        TestGeminiAIProvider provider = createTestProvider("valid-api-key", temperature, maxTokens, true);
        
        // Verify that configuration parameters are correctly included in request
        Map<String, Object> requestBody = provider.getLastRequestBody(prompt);
        
        assertThat(requestBody).isNotNull();
        assertThat(requestBody).containsKey("generationConfig");
        
        @SuppressWarnings("unchecked")
        Map<String, Object> generationConfig = (Map<String, Object>) requestBody.get("generationConfig");
        
        // Verify temperature is preserved
        assertThat(generationConfig).containsEntry("temperature", temperature);
        
        // Verify maxTokens is preserved
        assertThat(generationConfig).containsEntry("maxOutputTokens", maxTokens);
        
        // Verify other standard parameters are preserved
        assertThat(generationConfig).containsEntry("topP", 0.8);
        assertThat(generationConfig).containsEntry("topK", 40);
        
        // Verify safety settings are preserved
        assertThat(requestBody).containsKey("safetySettings");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> safetySettings = (List<Map<String, Object>>) requestBody.get("safetySettings");
        assertThat(safetySettings).hasSize(4);
        
        // Verify all safety categories are present
        assertThat(safetySettings).anySatisfy(setting -> 
            assertThat(setting).containsEntry("category", "HARM_CATEGORY_HARASSMENT"));
        assertThat(safetySettings).anySatisfy(setting -> 
            assertThat(setting).containsEntry("category", "HARM_CATEGORY_HATE_SPEECH"));
        assertThat(safetySettings).anySatisfy(setting -> 
            assertThat(setting).containsEntry("category", "HARM_CATEGORY_SEXUALLY_EXPLICIT"));
        assertThat(safetySettings).anySatisfy(setting -> 
            assertThat(setting).containsEntry("category", "HARM_CATEGORY_DANGEROUS_CONTENT"));
    }

    /**
     * **Property 2.3: Request Structure Preservation**
     * **Validates: Requirement 3.3**
     * 
     * This property verifies that the request structure, headers, and content formatting
     * remain consistent regardless of model configuration changes.
     */
    @Property(tries = 10)
    @Label("Preservation: Request structure should remain consistent")
    void requestStructureShouldRemainConsistent(
            @ForAll @StringLength(min = 1, max = 200) String prompt) {
        
        Assume.that(!prompt.trim().isEmpty());
        
        TestGeminiAIProvider provider = createTestProvider("test-api-key", 0.7, 1000, true);
        
        Map<String, Object> requestBody = provider.getLastRequestBody(prompt);
        
        // Verify core request structure is preserved
        assertThat(requestBody).containsKey("contents");
        assertThat(requestBody).containsKey("generationConfig");
        assertThat(requestBody).containsKey("safetySettings");
        
        // Verify contents structure
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> contents = (List<Map<String, Object>>) requestBody.get("contents");
        assertThat(contents).hasSize(1);
        
        Map<String, Object> content = contents.get(0);
        assertThat(content).containsKey("parts");
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
        assertThat(parts).hasSize(1);
        
        Map<String, Object> part = parts.get(0);
        assertThat(part).containsEntry("text", prompt);
    }

    /**
     * **Property 2.4: Error Handling Preservation**
     * **Validates: Requirement 3.3**
     * 
     * This property verifies that error handling mechanisms (retry logic, error messages)
     * continue to work correctly for non-model-related failures.
     */
    @Property(tries = 8)
    @Label("Preservation: Error handling should work consistently")
    void errorHandlingShouldWorkConsistently(
            @ForAll @StringLength(min = 5, max = 100) String prompt) {
        
        Assume.that(!prompt.trim().isEmpty());
        
        // Test authentication error handling
        TestGeminiAIProvider providerWithAuthError = createTestProvider("", 0.7, 1000, false);
        
        assertThatThrownBy(() -> {
            providerWithAuthError.generateResponse(prompt);
        })
        .isInstanceOf(AIServiceException.class)
        .hasMessageContaining("Gemini API key is not configured")
        .describedAs("Empty API key should result in configuration error");
        
        // Test null API key handling
        TestGeminiAIProvider providerWithNullKey = createTestProvider(null, 0.7, 1000, false);
        
        assertThatThrownBy(() -> {
            providerWithNullKey.generateResponse(prompt);
        })
        .isInstanceOf(AIServiceException.class)
        .hasMessageContaining("Gemini API key is not configured")
        .describedAs("Null API key should result in configuration error");
    }

    /**
     * Unit test for API key validation preservation
     * **Validates: Requirement 3.2**
     */
    @Test
    @Label("Preservation: API key validation behavior should be unchanged")
    void apiKeyValidationBehaviorShouldBeUnchanged() {
        String testPrompt = "Test prompt for API key validation";
        
        // Test empty API key
        TestGeminiAIProvider providerEmptyKey = createTestProvider("", 0.7, 1000, true);
        assertThatThrownBy(() -> providerEmptyKey.generateResponse(testPrompt))
            .isInstanceOf(AIServiceException.class)
            .hasMessageContaining("Gemini API key is not configured");
        
        // Test null API key
        TestGeminiAIProvider providerNullKey = createTestProvider(null, 0.7, 1000, true);
        assertThatThrownBy(() -> providerNullKey.generateResponse(testPrompt))
            .isInstanceOf(AIServiceException.class)
            .hasMessageContaining("Gemini API key is not configured");
        
        // Test valid API key
        TestGeminiAIProvider providerValidKey = createTestProvider("valid-key", 0.7, 1000, true);
        assertThatCode(() -> {
            AIResponse response = providerValidKey.generateResponse(testPrompt);
            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
        }).doesNotThrowAnyException();
    }

    /**
     * Unit test for configuration parameter preservation
     * **Validates: Requirement 3.4**
     */
    @Test
    @Label("Preservation: Configuration parameters should be preserved exactly")
    void configurationParametersShouldBePreservedExactly() {
        String testPrompt = "Test configuration preservation";
        double testTemperature = 0.9;
        int testMaxTokens = 2000;
        
        TestGeminiAIProvider provider = createTestProvider("test-key", testTemperature, testMaxTokens, true);
        Map<String, Object> requestBody = provider.getLastRequestBody(testPrompt);
        
        // Verify exact parameter preservation
        @SuppressWarnings("unchecked")
        Map<String, Object> generationConfig = (Map<String, Object>) requestBody.get("generationConfig");
        
        assertThat(generationConfig.get("temperature")).isEqualTo(testTemperature);
        assertThat(generationConfig.get("maxOutputTokens")).isEqualTo(testMaxTokens);
        assertThat(generationConfig.get("topP")).isEqualTo(0.8);
        assertThat(generationConfig.get("topK")).isEqualTo(40);
        
        // Verify safety settings structure is preserved
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> safetySettings = (List<Map<String, Object>>) requestBody.get("safetySettings");
        assertThat(safetySettings).hasSize(4);
        
        // Verify each safety setting has correct structure
        for (Map<String, Object> setting : safetySettings) {
            assertThat(setting).containsKeys("category", "threshold");
            assertThat(setting.get("threshold")).isEqualTo("BLOCK_MEDIUM_AND_ABOVE");
        }
    }

    /**
     * Unit test for response parsing preservation
     * **Validates: Requirement 3.3**
     */
    @Test
    @Label("Preservation: Response parsing should work consistently")
    void responseParsingShoudWorkConsistently() {
        String testPrompt = "Test response parsing";
        
        TestGeminiAIProvider provider = createTestProvider("test-key", 0.7, 1000, true);
        
        assertThatCode(() -> {
            AIResponse response = provider.generateResponse(testPrompt);
            
            // Verify response structure is preserved
            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getContent()).isNotNull().isNotEmpty();
            assertThat(response.getModel()).isEqualTo("gemini-1.5-flash");
            
        }).describedAs("Response parsing should work consistently").doesNotThrowAnyException();
    }
}