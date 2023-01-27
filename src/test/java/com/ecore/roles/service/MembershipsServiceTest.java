package com.ecore.roles.service;

import com.ecore.roles.client.model.Team;
import com.ecore.roles.exception.ResourceExistsException;
import com.ecore.roles.model.Membership;
import com.ecore.roles.model.Role;
import com.ecore.roles.repository.MembershipRepository;
import com.ecore.roles.repository.RoleRepository;
import com.ecore.roles.service.impl.MembershipsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static com.ecore.roles.utils.TestData.DEFAULT_MEMBERSHIP;
import static com.ecore.roles.utils.TestData.ORDINARY_CORAL_LYNX_TEAM;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MembershipsServiceTest {

    @InjectMocks
    private MembershipsServiceImpl membershipsService;
    @Mock
    private MembershipRepository membershipRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private TeamsService teamsService;

    @Test
    void shouldCreateMembership() {
        Membership expectedMembership = DEFAULT_MEMBERSHIP();
        when(roleRepository.findById(expectedMembership.getRole().getId()))
                .thenReturn(Optional.of(expectedMembership.getRole()));
        when(membershipRepository.existsByUserIdAndTeamIdAndRole(expectedMembership.getUserId(),
                expectedMembership.getTeamId(), expectedMembership.getRole()))
                        .thenReturn(false);
        when(membershipRepository
                .save(expectedMembership))
                        .thenReturn(expectedMembership);
        Team team = ORDINARY_CORAL_LYNX_TEAM();
        when(teamsService
                .getTeam(expectedMembership.getTeamId()))
                        .thenReturn(team);

        Membership actualMembership = membershipsService.assignRoleToMembership(expectedMembership);

        assertNotNull(actualMembership);
        assertEquals(actualMembership, expectedMembership);
        verify(roleRepository).findById(expectedMembership.getRole().getId());
        verify(membershipRepository).existsByUserIdAndTeamIdAndRole(expectedMembership.getUserId(),
                expectedMembership.getTeamId(), expectedMembership.getRole());
        verify(membershipRepository).save(expectedMembership);
        verify(teamsService).getTeam(expectedMembership.getTeamId());
    }

    @Test
    void shouldFailToCreateMembershipWhenMembershipsIsNull() {
        assertThrows(NullPointerException.class,
                () -> membershipsService.assignRoleToMembership(null));
    }

    @Test
    void shouldFailToCreateMembershipWhenItExists() {
        Membership expectedMembership = DEFAULT_MEMBERSHIP();
        when(roleRepository.findById(expectedMembership.getRole().getId()))
                .thenReturn(Optional.of(expectedMembership.getRole()));
        when(membershipRepository.existsByUserIdAndTeamIdAndRole(expectedMembership.getUserId(),
                expectedMembership.getTeamId(), expectedMembership.getRole()))
                        .thenReturn(true);

        ResourceExistsException exception = assertThrows(ResourceExistsException.class,
                () -> membershipsService.assignRoleToMembership(expectedMembership));

        assertEquals("Membership already exists", exception.getMessage());
        verify(roleRepository).findById(expectedMembership.getRole().getId());
        verify(membershipRepository).existsByUserIdAndTeamIdAndRole(expectedMembership.getUserId(),
                expectedMembership.getTeamId(), expectedMembership.getRole());
        verify(teamsService, never()).getTeam(any());
        verify(membershipRepository, never()).save(any());
    }

    @Test
    void shouldFailToGetMembershipsWhenRoleIdIsNull() {
        assertThrows(NullPointerException.class,
                () -> membershipsService.getMemberships(null));
    }

    @Test
    void shouldSuccessToGetRoles() {
        Membership expectedMembership = DEFAULT_MEMBERSHIP();
        List<Role> expectedMembershipRole = List.of(expectedMembership.getRole());
        when(membershipRepository.findByUserIdAndTeamId(expectedMembership.getUserId(),
                expectedMembership.getTeamId()))
                        .thenReturn(expectedMembershipRole);

        List<Role> roles =
                membershipsService.getRoles(expectedMembership.getUserId(), expectedMembership.getTeamId());

        assertNotNull(roles);
        assertEquals(roles, expectedMembershipRole);
        verify(membershipRepository).findByUserIdAndTeamId(expectedMembership.getUserId(),
                expectedMembership.getTeamId());
    }

    @ParameterizedTest
    @MethodSource("provideUserAndTeamForIsBlankOrNull")
    void shouldFailToGetRoles(UUID userId, UUID teamId) {
        assertThrows(NullPointerException.class,
                () -> membershipsService.getRoles(userId, teamId));

        verify(membershipRepository, never()).findByUserIdAndTeamId(userId,
                teamId);
    }

    public static Stream<Arguments> provideUserAndTeamForIsBlankOrNull() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(null, UUID.fromString("f037b4f9-cfd3-4988-bf06-d17ef388c7bc")),
                Arguments.of(UUID.fromString("fe1232da-7625-42a2-a1bd-db0352b645e1"), null));
    }

}
