package com.ecore.roles.web.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static com.ecore.roles.util.MessageConstraintViolationMatcher.messageIs;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;

class MembershipDtoTest {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void shouldFailToValidateMembershipWhenItHasInvalidRole() {
        MembershipDto membership = new MembershipDto();
        membership.setRoleId(null);
        Set<ConstraintViolation<MembershipDto>> violations = validator.validate(membership);
        assertThat(violations, hasItem(messageIs("Role is required")));
    }

    @Test
    void shouldFailToValidateMembershipWhenItHasInvalidUser() {
        MembershipDto membership = new MembershipDto();
        membership.setUserId(null);
        Set<ConstraintViolation<MembershipDto>> violations = validator.validate(membership);
        assertThat(violations, hasItem(messageIs("User is required")));
    }

    @Test
    void shouldFailToValidateMembershipWhenItHasInvalidTeam() {
        MembershipDto membership = new MembershipDto();
        membership.setTeamId(null);
        Set<ConstraintViolation<MembershipDto>> violations = validator.validate(membership);
        assertThat(violations, hasItem(messageIs("Team is required")));
    }
}
