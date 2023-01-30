package com.ecore.roles.service.impl;

import com.ecore.roles.client.model.Team;
import com.ecore.roles.exception.InvalidArgumentException;
import com.ecore.roles.exception.InvalidMembershipException;
import com.ecore.roles.exception.ResourceExistsException;
import com.ecore.roles.exception.ResourceNotFoundException;
import com.ecore.roles.model.Membership;
import com.ecore.roles.model.Role;
import com.ecore.roles.repository.MembershipRepository;
import com.ecore.roles.repository.RoleRepository;
import com.ecore.roles.service.MembershipsService;
import com.ecore.roles.service.TeamsService;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static java.util.Optional.ofNullable;

@Log4j2
@Service
public class MembershipsServiceImpl implements MembershipsService {

    private final MembershipRepository membershipRepository;
    private final TeamsService teamsService;
    private final RoleRepository roleRepository;

    public MembershipsServiceImpl(
            MembershipRepository membershipRepository,
            TeamsService teamsService,
            RoleRepository roleRepository) {
        this.membershipRepository = membershipRepository;
        this.teamsService = teamsService;
        this.roleRepository = roleRepository;
    }

    @Override
    public Membership assignRoleToMembership(@NonNull Membership membership) {
        UUID roleId = ofNullable(membership.getRole()).map(Role::getId)
                .orElseThrow(() -> new InvalidArgumentException(Role.class));
        validateMemberShip(membership);
        validateRole(roleId);
        validateTeam(membership.getTeamId(), membership.getUserId());
        return membershipRepository.save(membership);
    }

    @Override
    public Membership getByUserIdAndTeamId(@NonNull UUID userId, @NonNull UUID teamId) {
        return membershipRepository.findByUserIdAndTeamId(userId, teamId)
                .orElseThrow(() -> new ResourceExistsException(Membership.class));
    }

    @Override
    public List<Membership> getMemberships(@NonNull UUID id) {
        return membershipRepository.findByRoleId(id);
    }

    private void validateMemberShip(Membership membership) {
        if (membershipRepository.findByUserIdAndTeamId(membership.getUserId(), membership.getTeamId())
                .isPresent()) {
            throw new ResourceExistsException(Membership.class);
        }
    }

    private void validateTeam(UUID teamId, UUID userId) {
        Team team = teamsService.getTeam(teamId);
        if (!team.getTeamMemberIds().contains(userId)) {
            throw new InvalidMembershipException(Membership.class);
        }
    }

    private void validateRole(UUID roleId) {
        roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException(Role.class, roleId));
    }
}
