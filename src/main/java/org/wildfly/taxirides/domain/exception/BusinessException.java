package org.wildfly.taxirides.domain.exception;

import jakarta.ejb.ApplicationException;

@ApplicationException
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
