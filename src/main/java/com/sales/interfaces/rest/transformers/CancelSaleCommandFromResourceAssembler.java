package com.sales.interfaces.rest.transformers;

import com.sales.domain.model.commands.CancelSaleCommand;
import com.sales.interfaces.rest.resources.CancelSaleResource;

public class CancelSaleCommandFromResourceAssembler {
    public static CancelSaleCommand toCommand(Long id, CancelSaleResource r){
        return new CancelSaleCommand(id, r == null ? null : r.reason());
    }
}