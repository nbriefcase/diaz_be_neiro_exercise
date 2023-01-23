package com.ecore.roles.service.impl;

import com.ecore.roles.client.model.Team;
import com.ecore.roles.exception.InvalidArgumentException;
import com.ecore.roles.exception.ResourceExistsException;
import com.ecore.roles.exception.ResourceNotFoundException;
import com.ecore.roles.model.Membership;
import com.ecore.roles.model.Role;
import com.ecore.roles.repository.MembershipRepository;
import com.ecore.roles.service.MembershipsService;
import com.ecore.roles.service.RolesService;
import com.ecore.roles.service.TeamsService;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.ofNullable;

@Log4j2
@Service
public class MembershipsServiceImpl implements MembershipsService {

    private final MembershipRepository membershipRepository;
    private final RolesService rolesService;
    private final TeamsService teamsService;

    @Autowired
    public MembershipsServiceImpl(
            MembershipRepository membershipRepository,
            RolesService rolesService,
            TeamsService teamsService) {
        this.membershipRepository = membershipRepository;
        this.rolesService = rolesService;
        this.teamsService = teamsService;
    }

    @Override
    public Membership assignRoleToMembership(@NonNull Membership m) {

        UUID roleId = ofNullable(m.getRole()).map(Role::getId)
                .orElseThrow(() -> new InvalidArgumentException(Role.class));

        if (membershipRepository.findByUserIdAndTeamId(m.getUserId(), m.getTeamId())
                .isPresent()) {
            throw new ResourceExistsException(Membership.class);
        }

        validateMembership(m.getUserId(), m.getTeamId());

        rolesService.getRole(roleId);
        return membershipRepository.save(m);
    }

    @Override
    public List<Membership> getMemberships(@NonNull UUID rid) {
        return membershipRepository.findByRoleId(rid);
    }

    @Override
    public Optional<Membership> getMembership(UUID teamMemberId, UUID teamId) {
        validateMembership(teamMemberId, teamId);
        return membershipRepository.findByUserIdAndTeamId(teamMemberId, teamId);
    }

    private void validateMembership(UUID teamMemberId, UUID teamId) {
        Optional.ofNullable(teamsService.getTeam(teamId))
                .orElseThrow(() -> new ResourceNotFoundException(Team.class, teamId))
                .getTeamMemberIds().stream().filter(memberId -> memberId.equals(teamMemberId))
                .findFirst()
                .orElseThrow(() -> new InvalidArgumentException(Membership.class,
                        "The provided user doesn't belong to the provided team."));
    }
}
