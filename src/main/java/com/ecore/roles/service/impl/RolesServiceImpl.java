package com.ecore.roles.service.impl;

import com.ecore.roles.exception.ResourceExistsException;
import com.ecore.roles.exception.ResourceNotFoundException;
import com.ecore.roles.model.Role;
import com.ecore.roles.repository.RoleRepository;
import com.ecore.roles.service.MembershipsService;
import com.ecore.roles.service.RolesService;
import com.ecore.roles.service.TeamsService;
import com.ecore.roles.service.UsersService;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Log4j2
@Service
public class RolesServiceImpl implements RolesService {

    public static final String DEFAULT_ROLE = "Developer";

    private final RoleRepository roleRepository;

    private final MembershipsService membershipService;

    private final TeamsService teamsService;

    private final UsersService usersService;

    public RolesServiceImpl(
            RoleRepository roleRepository,
            MembershipsService membershipService,
            TeamsService teamsService,
            UsersService usersService) {
        this.roleRepository = roleRepository;
        this.membershipService = membershipService;
        this.teamsService = teamsService;
        this.usersService = usersService;
    }

    @Override
    public Role CreateRole(@NonNull Role role) {
        if (roleRepository.findByName(role.getName()).isPresent()) {
            throw new ResourceExistsException(Role.class);
        }
        return roleRepository.save(role);
    }

    @Override
    public Role GetRole(@NonNull UUID id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Role.class, id));
    }

    @Override
    public List<Role> GetRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role getRoleByUserAndTeam(@NonNull UUID userId, @NonNull UUID teamId) {
        teamsService.getTeam(teamId);
        usersService.getUser(userId);
        return membershipService.getByUserIdAndTeamId(userId, teamId).getRole();
    }

    private Role getDefaultRole() {
        return roleRepository.findByName(DEFAULT_ROLE)
                .orElseThrow(() -> new IllegalStateException("Default role is not configured"));
    }
}
