package com.ecore.roles.web.rest;

import com.ecore.roles.client.model.User;
import com.ecore.roles.service.UsersService;
import com.ecore.roles.web.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.ecore.roles.utils.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsersRestControllerTest {
    @InjectMocks
    private UsersRestController usersController;
    @Mock
    private UsersService usersService;

    @Test
    void shouldGetUsersWhenUserIdExists() {
        User gianniUser = GIANNI_USER();
        UserDto dto = GIANNI_USER_DTO();
        when(usersService.getUsers())
                .thenReturn(List.of(gianniUser));

        assertThat(dto).isIn(usersController.getUsers().getBody());
    }

    @Test
    void shouldGetUserWhenUserIdExists() {
        User gianniUser = GIANNI_USER();
        when(usersService.getUser(UUID_1))
                .thenReturn(gianniUser);

        assertNotNull(usersController.getUser(UUID_1));
    }

}
