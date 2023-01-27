package com.ecore.roles.web;

import com.ecore.roles.web.dto.RoleDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

public interface RolesApi {

    ResponseEntity<RoleDto> createRole(
            RoleDto role,
            UriComponentsBuilder uriBuilder);

    ResponseEntity<List<RoleDto>> getRoles();

    ResponseEntity<RoleDto> getRole(
            UUID roleId);

    ResponseEntity<List<RoleDto>> getRolesByUserAndTeam(
            UUID userId,
            UUID teamId);
}
