package org.wildfly.taxirides.api.exceptionhandler;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.wildfly.taxirides.domain.exception.CouldNotSaveDriverException;

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
        return  generateResponse(exception, status);
    }

    private Response.Status getExceptionStatus(Throwable exception) {
        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;

        if (exception instanceof CouldNotSaveDriverException) {
            status = Response.Status.NOT_FOUND;
        }
//        else if (exception instanceof Exception) {
//            status = Response.Status.BAD_REQUEST;
//        }

        return  status;
    }

    public Response generateResponse(Throwable exception, Response.Status status) {
        Map<String, String> response = new HashMap<>();
        response.put("message", exception.getMessage());
        response.put("exception", exception.getClass().getName());

        return Response.status(status)
                .entity(response)
                .build();
    }
}
