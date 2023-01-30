package com.ecore.roles.web.rest;

import com.ecore.roles.service.RolesService;
import com.ecore.roles.web.RolesApi;
import com.ecore.roles.web.dto.RoleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ecore.roles.web.dto.RoleDto.fromModel;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/roles")
public class RolesRestController implements RolesApi {

    private final RolesService rolesService;

    @Override
    @PostMapping(
            consumes = {"application/json"},
            produces = {"application/json"})
    public ResponseEntity<RoleDto> createRole(
            @Valid @RequestBody RoleDto role) {
        return ResponseEntity
                .status(HttpStatus.CREATED.value())
                .body(fromModel(rolesService.CreateRole(role.toModel())));
    }

    @Override
    @GetMapping(
            produces = {"application/json"})
    public ResponseEntity<List<RoleDto>> getRoles() {
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(rolesService.GetRoles()
                        .stream().map(role -> fromModel(role)).collect(Collectors.toList()));
    }

    @Override
    @GetMapping(path = "/{roleId}")
    public ResponseEntity<RoleDto> getRole(
            @PathVariable UUID roleId) {
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(fromModel(rolesService.GetRole(roleId)));
    }

    @Override
    @GetMapping(path = "/search")
    public ResponseEntity<RoleDto> getRolesByUserAndTeam(
            @RequestParam UUID userId,
            @RequestParam UUID teamId) {
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(fromModel(rolesService.getRoleByUserAndTeam(userId, teamId)));
    }

}
