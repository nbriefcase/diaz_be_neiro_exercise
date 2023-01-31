package com.ecore.roles.configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "retry")
public class RetryConfigurationProperties {

    private int maxAttempts;
    private long initialInterval;
    private double multiplier;
    private long maxInterval;
    private boolean withRandom;

}
