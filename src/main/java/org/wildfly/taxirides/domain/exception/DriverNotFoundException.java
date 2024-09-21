package org.wildfly.taxirides.domain.exception;

import jakarta.ejb.ApplicationException;
import jakarta.persistence.EntityNotFoundException;

@ApplicationException
public class DriverNotFoundException extends EntityNotFoundException {
    public DriverNotFoundException(String message) {
        super(message);
    }

    public DriverNotFoundException(Long id) {
        this(String.format("There is no driver for id %d", id));
    }
}
