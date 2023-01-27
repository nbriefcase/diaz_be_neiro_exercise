package com.ecore.roles.service.impl;

import com.ecore.roles.client.model.Team;
import com.ecore.roles.exception.InvalidArgumentException;
import com.ecore.roles.exception.ResourceExistsException;
import com.ecore.roles.exception.ResourceNotFoundException;
import com.ecore.roles.model.Membership;
import com.ecore.roles.model.Role;
import com.ecore.roles.repository.MembershipRepository;
import com.ecore.roles.repository.RoleRepository;
import com.ecore.roles.service.MembershipsService;
import com.ecore.roles.service.TeamsService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class MembershipsServiceImpl implements MembershipsService {

    private final MembershipRepository membershipRepository;
    private final RoleRepository roleRepository;
    private final TeamsService teamsService;

    @Override
    public Membership assignRoleToMembership(@NonNull @Valid Membership membership) {
        validateResourceExists(membership);
        validateTeamMember(membership);
        return membershipRepository.save(membership);
    }

    private void validateResourceExists(Membership membership) {
        UUID roleId = membership.getRole().getId();
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException(Role.class, roleId));
        if (membershipRepository.existsByUserIdAndTeamIdAndRole(membership.getUserId(),
                membership.getTeamId(), role)) {
            throw new ResourceExistsException(Membership.class);
        }
    }

    private void validateTeamMember(Membership membership) {
        Team team = teamsService.getTeam(membership.getTeamId());
        if (team == null) {
            throw new ResourceNotFoundException(Team.class, membership.getTeamId());
        }
        Objects.requireNonNull(team).getTeamMemberIds().stream()
                .filter(p -> p.equals(membership.getUserId()))
                .findAny()
                .orElseThrow(() -> new InvalidArgumentException(
                        "Invalid 'Membership' object. The provided user doesn't belong to the provided team."));
    }

    @Override
    public List<Membership> getMemberships(@NonNull UUID roleId) {
        return membershipRepository.findByRoleId(roleId);
    }

    @Override
    public List<Role> getRoles(@NonNull UUID userId, @NonNull UUID teamId) {
        return membershipRepository.findByUserIdAndTeamId(userId, teamId);
    }
}
