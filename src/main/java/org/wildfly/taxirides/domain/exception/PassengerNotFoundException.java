package org.wildfly.taxirides.domain.exception;

import jakarta.ejb.ApplicationException;

@ApplicationException
public class PassengerNotFoundException extends EntityNotFoundException {
    public PassengerNotFoundException(String message) {
        super(message);
    }

    public PassengerNotFoundException(Long id) {
        this(String.format("There is no passenger for id %d", id));
    }
}
