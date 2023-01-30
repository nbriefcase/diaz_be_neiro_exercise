package com.ecore.roles.web.rest;

import com.ecore.roles.service.MembershipsService;
import com.ecore.roles.web.MembershipsApi;
import com.ecore.roles.web.dto.MembershipDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ecore.roles.web.dto.MembershipDto.fromModel;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/memberships")
public class MembershipsRestController implements MembershipsApi {

    private final MembershipsService membershipsService;

    @Override
    @PostMapping
    public ResponseEntity<MembershipDto> assignRoleToMembership(
            @NotNull @Valid @RequestBody MembershipDto membershipDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED.value())
                .body(fromModel(membershipsService.assignRoleToMembership(membershipDto.toModel())));
    }

    @Override
    @GetMapping(path = "/search")
    public ResponseEntity<List<MembershipDto>> getMemberships(
            @RequestParam UUID roleId) {
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(membershipsService.getMemberships(roleId)
                        .stream().map(membership -> fromModel(membership)).collect(Collectors.toList()));
    }

}
