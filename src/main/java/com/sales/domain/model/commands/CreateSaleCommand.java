package com.sales.domain.model.commands;

import java.util.List;

public record CreateSaleCommand(
        Long vehicleId,
        List<String> initialImages
) {}