package org.wildfly.taxirides.api.exceptionhandler;

import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.wildfly.taxirides.domain.exception.BusinessException;

import java.util.HashMap;
import java.util.Map;

@Provider
public class ApiExceptionHandler implements ExceptionMapper<Throwable> {

    @Inject
    private Logger log;

    @Override
    public Response toResponse(Throwable exception) {
        logException(exception);
        return findExceptionTypeAndHandle(exception);
    }

    private void logException(Throwable exception) {
        log.error("Exception occurred: ", exception);
    }

    private Response findExceptionTypeAndHandle(Throwable exception) {
        Response.Status status = getExceptionStatus(exception);
        return buildErrorResponse(exception, status);
    }

    private Response.Status getExceptionStatus(Throwable exception) {
        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;

        if (exception instanceof EntityNotFoundException) {
            status = Response.Status.NOT_FOUND;
        } else if (exception instanceof NotFoundException) {
            status = Response.Status.NOT_FOUND;
        } else if (exception instanceof BusinessException) {
            status = Response.Status.BAD_REQUEST;
        }

        return status;
    }

    public Response buildErrorResponse(Throwable exception, Response.Status status) {
        Map<String, String> response = new HashMap<>();
        response.put("message", Response.Status.INTERNAL_SERVER_ERROR.equals(status) ? getFriendlyInternalErroMsg()
                : exception.getMessage());
        response.put("exception", exception.getClass().getName());
        response.put("errorCode", Integer.toString(status.getStatusCode()));

        return Response.status(status)
                .entity(response)
                .build();
    }

    private String getFriendlyInternalErroMsg() {
        return "An unexpected internal error has occurred in the system. " +
                "Try again and if the problem persists, contact your system administrator.";
    }
}
