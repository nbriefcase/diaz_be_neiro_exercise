package com.ecore.roles.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"role_id", "team_id", "user_id"}))
public class Membership {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-char")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    @NotNull(message = "Role is required")
    private Role role;

    @Column(name = "user_id", nullable = false)
    @NotNull(message = "User is required")
    private UUID userId;

    @Column(name = "team_id", nullable = false)
    @NotNull(message = "Team is required")
    private UUID teamId;

}
