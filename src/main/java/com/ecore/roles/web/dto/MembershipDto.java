package com.ecore.roles.web.dto;

import com.ecore.roles.model.Membership;
import com.ecore.roles.model.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.UUID;

import static java.util.Optional.ofNullable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MembershipDto {

    @JsonProperty
    private UUID id;

    @JsonProperty
    @NotNull(message = "Role is required")
    @EqualsAndHashCode.Include
    private UUID roleId;

    @JsonProperty(value = "teamMemberId")
    @NotNull(message = "User is required")
    @EqualsAndHashCode.Include
    private UUID userId;

    @JsonProperty
    @NotNull(message = "Team is required")
    @EqualsAndHashCode.Include
    private UUID teamId;

    public static MembershipDto fromModel(Membership membership) {
        if (membership == null) {
            return null;
        }
        return MembershipDto.builder()
                .id(membership.getId())
                .roleId(ofNullable(membership.getRole()).map(Role::getId).orElse(null))
                .userId(membership.getUserId())
                .teamId(membership.getTeamId())
                .build();
    }

    public Membership toModel() {
        return Membership.builder()
                .id(this.id)
                .role(Role.builder().id(this.roleId).build())
                .userId(this.userId)
                .teamId(this.teamId)
                .build();
    }

}
