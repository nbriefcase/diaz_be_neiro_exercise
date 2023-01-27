package com.ecore.roles.client.model;

import lombok.*;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Team {

    private UUID id;

    private String name;

    private UUID teamLeadId;

    private List<UUID> teamMemberIds;
}
