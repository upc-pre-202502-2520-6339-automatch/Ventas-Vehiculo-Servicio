package com.sales.interfaces.rest.transformers;

import com.sales.domain.model.aggregates.SaleOrder;
import com.sales.interfaces.rest.resources.SaleResource;

import java.util.stream.Collectors;

public class SaleResourceFromEntityAssembler {
    public static SaleResource toResourceFromEntity(SaleOrder s) {
        var imgs = s.getImages().stream().map(i -> i.getValue()).collect(Collectors.toList());
        return new SaleResource(
                s.getId(),
                s.getVehicleId(),
                s.getBuyerId(),
                s.getSellerId(),
                s.getPriceAmount(),
                s.getPriceCurrency(),
                s.getStatus(),
                s.getReservationExpiresAt(),
                imgs
        );
    }
}
