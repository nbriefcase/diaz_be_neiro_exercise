package com.ecore.roles.api;

import com.ecore.roles.client.model.User;
import com.ecore.roles.utils.RestAssuredHelper;
import com.ecore.roles.web.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static com.ecore.roles.utils.MockUtils.mockGetUserById;
import static com.ecore.roles.utils.MockUtils.mockGetUsers;
import static com.ecore.roles.utils.RestAssuredHelper.getUser;
import static com.ecore.roles.utils.RestAssuredHelper.getUsers;
import static com.ecore.roles.utils.TestData.GIANNI_USER;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsersApiTest {

    private final RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @LocalServerPort
    private int port;

    @Autowired
    public UsersApiTest(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        RestAssuredHelper.setUp(port);
    }

    @Test
    void shouldGetUserById() {
        User expectedUser = GIANNI_USER();
        mockGetUserById(mockServer, expectedUser.getId(), expectedUser);

        UserDto userDto = getUser(expectedUser.getId())
                .extract().as(UserDto.class);

        assertThat(userDto).isNotNull();
        assertThat(userDto.getDisplayName()).isEqualTo(expectedUser.getDisplayName());
    }

    @Test
    void shouldGetAllUsers() {
        User expectedUser = GIANNI_USER();
        mockGetUsers(mockServer, expectedUser);

        UserDto[] users = getUsers()
                .extract().as(UserDto[].class);

        assertThat(users.length).isGreaterThanOrEqualTo(1);
        assertThat(users).contains(UserDto.fromModel(expectedUser));
    }

}
