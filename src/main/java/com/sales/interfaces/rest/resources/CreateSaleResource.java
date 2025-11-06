package com.sales.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CreateSaleResource(
        @NotNull Long vehicleId,
        List<String> images
) {}
