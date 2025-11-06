package com.sales.interfaces.rest.errors;

import java.time.Instant;
import java.util.List;

public record ApiError(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        List<FieldError> fieldErrors
) {
    public static record FieldError(String field, String message) {}
}
