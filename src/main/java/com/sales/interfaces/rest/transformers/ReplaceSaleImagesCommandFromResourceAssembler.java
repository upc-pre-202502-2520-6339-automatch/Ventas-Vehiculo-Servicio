// com.sales.interfaces.rest.transformers.ReplaceSaleImagesCommandFromResourceAssembler
package com.sales.interfaces.rest.transformers;

import com.sales.domain.model.commands.ReplaceSaleImagesCommand;
import com.sales.interfaces.rest.resources.ReplaceSaleImagesResource;

public class ReplaceSaleImagesCommandFromResourceAssembler {
    public static ReplaceSaleImagesCommand toCommandFromResource(Long saleId, ReplaceSaleImagesResource r) {
        return new ReplaceSaleImagesCommand(saleId, r.urls());
    }
}
