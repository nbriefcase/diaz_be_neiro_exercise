package com.ecore.roles.api;

import com.ecore.roles.client.model.Team;
import com.ecore.roles.utils.RestAssuredHelper;
import com.ecore.roles.web.dto.TeamDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static com.ecore.roles.utils.MockUtils.mockGetTeamById;
import static com.ecore.roles.utils.MockUtils.mockGetTeams;
import static com.ecore.roles.utils.RestAssuredHelper.getTeam;
import static com.ecore.roles.utils.RestAssuredHelper.getTeams;
import static com.ecore.roles.utils.TestData.ORDINARY_CORAL_LYNX_TEAM;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TeamsApiTest {

    private final RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @LocalServerPort
    private int port;

    @Autowired
    public TeamsApiTest(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        RestAssuredHelper.setUp(port);
    }

    @Test
    void shouldGetTeamById() {
        Team expectedTeam = ORDINARY_CORAL_LYNX_TEAM();
        mockGetTeamById(mockServer, expectedTeam.getId(), expectedTeam);

        TeamDto teamDto = getTeam(expectedTeam.getId())
                .extract().as(TeamDto.class);

        assertThat(teamDto).isNotNull();
        assertThat(teamDto.getId()).isEqualTo(expectedTeam.getId());
    }

    @Test
    void shouldGetAllTeams() {
        Team expectedTeam = ORDINARY_CORAL_LYNX_TEAM();
        mockGetTeams(mockServer, expectedTeam);

        TeamDto[] teams = getTeams()
                .extract().as(TeamDto[].class);

        assertThat(teams.length).isGreaterThanOrEqualTo(1);
        assertThat(teams).contains(TeamDto.fromModel(expectedTeam));
    }

}
