package org.wildfly.taxirides.domain.exception;

import jakarta.ejb.ApplicationException;

@ApplicationException
public class CouldNotSaveDriverException extends RuntimeException {
    public CouldNotSaveDriverException(String message) {
        super(message);
    }
}
