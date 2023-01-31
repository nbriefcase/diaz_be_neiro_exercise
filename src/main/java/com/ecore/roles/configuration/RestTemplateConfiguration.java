package com.ecore.roles.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class RestTemplateConfiguration {
    private final RetryConfigurationProperties retryConfigurationProperties;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public RetryTemplate retryTemplate() {
        return RetryTemplate.builder()
                .maxAttempts(retryConfigurationProperties.getMaxAttempts())
                .exponentialBackoff(retryConfigurationProperties.getInitialInterval(),
                        retryConfigurationProperties.getMultiplier(),
                        retryConfigurationProperties.getMaxInterval(),
                        retryConfigurationProperties.isWithRandom())
                .retryOn(HttpServerErrorException.class)
                .build();
    }

}
