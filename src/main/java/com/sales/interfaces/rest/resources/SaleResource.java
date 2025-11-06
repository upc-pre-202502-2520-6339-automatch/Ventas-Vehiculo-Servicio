package com.sales.interfaces.rest.resources;

import com.sales.domain.model.valueobjects.SaleStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record SaleResource(
        Long id,
        Long vehicleId,
        Long buyerId,
        Long sellerId,
        BigDecimal priceAmount,
        String priceCurrency,
        SaleStatus status,
        Instant reservationExpiresAt,
        List<String> images
) {}

