package com.sales.domain.exceptions;

public class BusinessRuleViolationException extends RuntimeException {
    public BusinessRuleViolationException(String msg){ super(msg); }
}