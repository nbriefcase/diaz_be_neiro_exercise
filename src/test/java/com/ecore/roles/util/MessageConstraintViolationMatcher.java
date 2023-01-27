package com.ecore.roles.util;

import org.hamcrest.Matcher;

import javax.validation.ConstraintViolation;

import static java.lang.String.format;

public final class MessageConstraintViolationMatcher {
    private MessageConstraintViolationMatcher() {}

    public static Matcher<ConstraintViolation<?>> messageIs(String message) {
        return new FuncTypeSafeMatcher<>(violation -> violation.getMessage().equals(message),
                (description) -> description.appendText(format("Message should be '%s'", message)),
                (violation, description) -> description
                        .appendText(format("Was '%s'", violation.getMessage())));
    }
}
