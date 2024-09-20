package org.wildfly.taxirides.api.exceptionhandler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();

        Map<String, String> errors = violations.stream()
                .collect(Collectors.toMap(
                        violation -> getSimplePropertyName(violation.getPropertyPath().toString()), // Extract readable field name
                        ConstraintViolation::getMessage // error message
                ));

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("error", "Validation failed");
        responseBody.put("details", errors);

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(responseBody)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    // Utility method to extract the simple field name
    private String getSimplePropertyName(String fullPropertyPath) {
        // Split by dots and return the last part (field name)
        String[] parts = fullPropertyPath.split("\\.");
        return parts[parts.length - 1];
    }
}
