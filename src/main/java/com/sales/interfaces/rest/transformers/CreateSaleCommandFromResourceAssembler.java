package com.sales.interfaces.rest.transformers;

import com.sales.domain.model.commands.CreateSaleCommand;
import com.sales.interfaces.rest.resources.CreateSaleResource;

public class CreateSaleCommandFromResourceAssembler {
    public static CreateSaleCommand toCommandFromResource(CreateSaleResource r) {
        return new CreateSaleCommand(r.vehicleId(), r.images());
    }
}