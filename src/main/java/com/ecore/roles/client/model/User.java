package com.ecore.roles.client.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class User {

    private UUID id;

    private String firstName;

    private String lastName;

    private String displayName;

    private String avatarUrl;

    private String location;
}
