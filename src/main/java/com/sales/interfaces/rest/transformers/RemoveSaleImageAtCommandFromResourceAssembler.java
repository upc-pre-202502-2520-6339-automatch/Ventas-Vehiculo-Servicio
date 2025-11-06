package com.sales.interfaces.rest.transformers;

import com.sales.domain.model.commands.RemoveSaleImageAtCommand;

public class RemoveSaleImageAtCommandFromResourceAssembler {
    public static RemoveSaleImageAtCommand at(Long saleId, int position) {
        return new RemoveSaleImageAtCommand(saleId, position);
    }
}
