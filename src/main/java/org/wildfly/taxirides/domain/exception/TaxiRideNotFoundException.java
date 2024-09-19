package org.wildfly.taxirides.domain.exception;

import jakarta.ejb.ApplicationException;

@ApplicationException
public class TaxiRideNotFoundException extends EntityNotFoundException {
    public TaxiRideNotFoundException(String message) {
        super(message);
    }

    public TaxiRideNotFoundException(Long id) {
        this(String.format("There is no taxi ride for id %d", id));
    }
}
