package com.ecore.roles.model;

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

class RoleTest {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void shouldFailToValidateRoleWhenItHasInvalidName() {
        Role role = new Role();
        role.setName(null);
        Set<ConstraintViolation<Role>> violations = validator.validate(role);
        assertThat(violations, hasItem(messageIs("Name must not be empty")));
    }

}
