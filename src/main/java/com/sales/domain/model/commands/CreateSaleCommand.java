package com.sales.domain.model.commands;

import java.math.BigDecimal;
import java.util.List;

public record CreateSaleCommand(
        Long vehicleId, Long buyerId, Long sellerId,
        BigDecimal priceAmount, String priceCurrency,
        List<String> initialImages
) {}
