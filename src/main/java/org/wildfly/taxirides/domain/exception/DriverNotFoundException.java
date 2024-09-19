package org.wildfly.taxirides.domain.exception;

import jakarta.ejb.ApplicationException;

@ApplicationException
public class DriverNotFoundException extends EntityNotFoundException {
    public DriverNotFoundException(String message) {
        super(message);
    }

    public DriverNotFoundException(Long id) {
        this(String.format("There is no driver for id %d", id));
    }
}
