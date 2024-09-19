package org.wildfly.taxirides.domain.exception;

import jakarta.ejb.ApplicationException;

@ApplicationException
public class EntityNotFoundException extends BusinessException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
