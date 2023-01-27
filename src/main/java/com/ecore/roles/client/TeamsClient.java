package com.ecore.roles.client;

import com.ecore.roles.client.model.Team;
import com.ecore.roles.configuration.ClientsConfigurationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class TeamsClient {

    private final RestTemplate restTemplate;
    private final RetryTemplate retryTemplate;
    private final ClientsConfigurationProperties clientsConfigurationProperties;

    public ResponseEntity<Team> getTeam(UUID id) {
        RetryCallback<ResponseEntity<Team>, RuntimeException> callback = context -> restTemplate.getForEntity(
                clientsConfigurationProperties.getTeamsApiHost() + "/{id}",
                Team.class,
                id);
        return retryTemplate.execute(callback);
    }

    public ResponseEntity<List<Team>> getTeams() {
        return retryTemplate.execute(context -> restTemplate.exchange(
                clientsConfigurationProperties.getTeamsApiHost(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}));
    }
}
