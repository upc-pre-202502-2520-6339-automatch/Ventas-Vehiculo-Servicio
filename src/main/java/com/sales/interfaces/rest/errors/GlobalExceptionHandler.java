package com.sales.interfaces.rest.errors;

import com.sales.domain.exceptions.BusinessRuleViolationException;
import com.sales.domain.exceptions.SaleNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<ApiError.FieldError> fields = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new ApiError.FieldError(fe.getField(), fe.getDefaultMessage()))
                .toList();
        return build(HttpStatus.BAD_REQUEST, "Validation Failed", "Input validation error", req, fields);
    }

    @ExceptionHandler({ ConstraintViolationException.class, HttpMessageNotReadableException.class, IllegalArgumentException.class })
    public ResponseEntity<ApiError> handleBadRequest(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage(), req, null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAny(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "Unexpected error", req, null);
    }

    @ExceptionHandler(SaleNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(SaleNotFoundException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), req, null);
    }
    @ExceptionHandler(BusinessRuleViolationException.class)
    public ResponseEntity<ApiError> handleBusiness(BusinessRuleViolationException ex, HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, "Conflict", ex.getMessage(), req, null);
    }




    private ResponseEntity<ApiError> build(HttpStatus status, String error, String message,
                                           HttpServletRequest req, List<ApiError.FieldError> fields) {
        ApiError body = new ApiError(Instant.now(), status.value(), error, message, req.getRequestURI(), fields);
        return ResponseEntity.status(status).contentType(MediaType.APPLICATION_JSON).body(body);
    }
}
