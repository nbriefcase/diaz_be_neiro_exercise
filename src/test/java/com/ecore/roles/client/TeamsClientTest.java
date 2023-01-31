package com.ecore.roles.client;

import com.ecore.roles.client.model.Team;
import com.ecore.roles.configuration.ClientsConfigurationProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.ecore.roles.utils.TestData.ORDINARY_CORAL_LYNX_TEAM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamsClientTest {
    private static final String TEAMS_API_HOST =
            "https://cgjresszgg.execute-api.eu-west-1.amazonaws.com/teams";
    @InjectMocks
    private TeamsClient teamsClient;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private RetryTemplate retryTemplate;
    @Mock
    private ClientsConfigurationProperties clientsConfigurationProperties;

    @Test
    void shouldGetTeamWhenTeamIdExists() {
        Team team = ORDINARY_CORAL_LYNX_TEAM();
        when(clientsConfigurationProperties.getTeamsApiHost()).thenReturn(TEAMS_API_HOST);
        when(retryTemplate.execute(any(), any(), any())).thenAnswer(invocation -> {
            RetryCallback<ResponseEntity<Team>, RuntimeException> retry = invocation.getArgument(0);
            return retry.doWithRetry(null);
        });
        when(restTemplate.getForEntity(eq(TEAMS_API_HOST + "/{id}"), eq(Team.class), eq(team.getId())))
                .thenReturn(ResponseEntity.ok(team));

        assertNotNull(teamsClient.getTeam(team.getId()));
    }

    @Test
    void shouldGetTeamsWhenTeamIdExists() {
        Team team = ORDINARY_CORAL_LYNX_TEAM();
        when(clientsConfigurationProperties.getTeamsApiHost()).thenReturn(TEAMS_API_HOST);
        when(retryTemplate.execute(any(), any(), any())).thenAnswer(invocation -> {
            RetryCallback<ResponseEntity<Team>, RuntimeException> retry = invocation.getArgument(0);
            return retry.doWithRetry(null);
        });
        when(restTemplate.exchange(eq(TEAMS_API_HOST), eq(HttpMethod.GET), eq(null),
                eq(new ParameterizedTypeReference<List<Team>>() {})))
                        .thenReturn(ResponseEntity.ok(List.of(team)));

        assertThat(team).isIn(teamsClient.getTeams().getBody());
    }

}
