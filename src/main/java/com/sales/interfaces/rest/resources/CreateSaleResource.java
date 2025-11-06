package com.sales.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.util.List;

public record CreateSaleResource(
        @NotNull Long vehicleId,
        @NotNull Long buyerId,
        @NotNull Long sellerId,
        @NotNull BigDecimal priceAmount,
        @Pattern(regexp="^[A-Z]{3}$") @NotBlank String priceCurrency,
        List<String> images  // opcional
) {}
