package com.ecore.roles.web.rest;

import com.ecore.roles.client.model.Team;
import com.ecore.roles.service.TeamsService;
import com.ecore.roles.web.dto.TeamDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.ecore.roles.utils.TestData.*;
import static com.ecore.roles.utils.TestData.UUID_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamsRestControllerTest {
    @InjectMocks
    private TeamsRestController teamsController;
    @Mock
    private TeamsService teamsService;

    @Test
    void shouldGetTeamsWhenTeamIdExists() {
        Team team = ORDINARY_CORAL_LYNX_TEAM();
        TeamDto dto = ORDINARY_CORAL_LYNX_TEAM_DTO();
        when(teamsService.getTeams())
                .thenReturn(List.of(team));

        assertThat(dto).isIn(teamsController.getTeams().getBody());
    }

    @Test
    void shouldGetTeamWhenTeamIdExists() {
        Team team = ORDINARY_CORAL_LYNX_TEAM();
        when(teamsService.getTeam(UUID_1))
                .thenReturn(team);

        assertNotNull(teamsController.getTeam(UUID_1));
    }

}
