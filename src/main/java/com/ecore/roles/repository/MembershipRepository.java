package com.ecore.roles.repository;

import com.ecore.roles.model.Membership;
import com.ecore.roles.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, UUID> {

    @Query("SELECT m.role FROM Membership m WHERE m.userId = :userId AND m.teamId = :teamId")
    List<Role> findByUserIdAndTeamId(UUID userId, UUID teamId);

    List<Membership> findByRoleId(UUID roleId);

    boolean existsByUserIdAndTeamIdAndRole(UUID userId, UUID teamId, Role role);
}
