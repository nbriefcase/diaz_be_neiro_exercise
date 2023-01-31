package com.ecore.roles.client;

import com.ecore.roles.client.model.User;
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

import static com.ecore.roles.utils.TestData.GIANNI_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsersClientTest {
    private static final String USERS_API_HOST =
            "https://cgjresszgg.execute-api.eu-west-1.amazonaws.com/users";
    @InjectMocks
    private UsersClient usersClient;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private RetryTemplate retryTemplate;
    @Mock
    private ClientsConfigurationProperties clientsConfigurationProperties;

    @Test
    void shouldGetUserWhenUserIdExists() {
        User gianniUser = GIANNI_USER();
        when(clientsConfigurationProperties.getUsersApiHost()).thenReturn(USERS_API_HOST);
        when(retryTemplate.execute(any(), any(), any())).thenAnswer(invocation -> {
            RetryCallback<ResponseEntity<User>, RuntimeException> retry = invocation.getArgument(0);
            return retry.doWithRetry(null);
        });
        when(restTemplate.exchange(eq(USERS_API_HOST + "/{id}"), eq(HttpMethod.GET), eq(null), eq(User.class),
                eq(gianniUser.getId())))
                        .thenReturn(ResponseEntity.ok(gianniUser));

        assertNotNull(usersClient.getUser(gianniUser.getId()));
    }

    @Test
    void shouldGetUsersWhenUserIdExists() {
        User gianniUser = GIANNI_USER();
        when(clientsConfigurationProperties.getUsersApiHost()).thenReturn(USERS_API_HOST);
        when(retryTemplate.execute(any(), any(), any())).thenAnswer(invocation -> {
            RetryCallback<ResponseEntity<User>, RuntimeException> retry = invocation.getArgument(0);
            return retry.doWithRetry(null);
        });
        when(restTemplate.exchange(
                eq(USERS_API_HOST),
                eq(HttpMethod.GET),
                eq(null),
                eq(new ParameterizedTypeReference<List<User>>() {})))
                        .thenReturn(ResponseEntity.ok(List.of(gianniUser)));

        assertThat(gianniUser).isIn(usersClient.getUsers().getBody());
    }

}
