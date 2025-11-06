package com.sales.interfaces.rest.resources;

import java.math.BigDecimal;

public record VehicleSummary(
        Long id,
        Long sellerId,
        BigDecimal priceAmount,
        String priceCurrency,
        String status,         // "PUBLISHED", "RESERVED", "SOLD", etc.
        String mainImageUrl
) {}