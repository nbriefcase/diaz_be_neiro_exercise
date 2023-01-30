package com.ecore.roles.exception;

import static java.lang.String.format;

public class InvalidMembershipException extends RuntimeException {

    public <T> InvalidMembershipException(Class<T> resource) {
        super(format("Invalid '%s' object. The provided user doesn't belong to the provided team.",
                resource.getSimpleName()));
    }
}
