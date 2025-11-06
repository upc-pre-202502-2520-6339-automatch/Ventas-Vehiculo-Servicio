package com.sales.interfaces.rest.transformers;

import com.sales.domain.model.commands.AddSaleImagesCommand;
import com.sales.interfaces.rest.resources.AddSaleImagesResource;

public class AddSaleImagesCommandFromResourceAssembler {
    public static AddSaleImagesCommand toCommandFromResource(Long saleId, AddSaleImagesResource r) {
        return new AddSaleImagesCommand(saleId, r.urls());
    }
}
