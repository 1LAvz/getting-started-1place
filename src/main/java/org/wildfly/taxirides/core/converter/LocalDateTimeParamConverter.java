package org.wildfly.taxirides.core.converter;

import jakarta.ws.rs.ext.ParamConverter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeParamConverter implements ParamConverter<LocalDateTime> {

    // Standard format: "yyyy-MM-ddTHH:mm:ss"
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public LocalDateTime fromString(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(value, FORMATTER);
    }

    @Override
    public String toString(LocalDateTime value) {
        if (value == null) {
            return null;
        }
        return value.format(FORMATTER);
    }
}