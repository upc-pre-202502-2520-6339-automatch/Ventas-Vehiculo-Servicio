package com.sales.domain.exceptions;

public class SaleNotFoundException extends RuntimeException {
    public SaleNotFoundException(Long id){ super("Sale %d not found".formatted(id)); }
}