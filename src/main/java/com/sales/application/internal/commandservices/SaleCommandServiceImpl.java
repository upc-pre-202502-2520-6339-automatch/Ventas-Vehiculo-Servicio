package com.sales.application.internal.commandservices;

import com.sales.domain.exceptions.BusinessRuleViolationException;
import com.sales.domain.exceptions.SaleNotFoundException;
import com.sales.domain.model.aggregates.SaleOrder;
import com.sales.domain.model.commands.CancelSaleCommand;
import com.sales.domain.model.commands.ConfirmPaymentCommand;
import com.sales.domain.model.commands.CreateSaleCommand;
import com.sales.domain.model.valueobjects.SaleStatus;
import com.sales.infrastructure.persistence.jpa.SaleOrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
public class SaleCommandServiceImpl {

    private static final int MAX_IMAGES = 10;
    private final SaleOrderRepository repo;

    public SaleCommandServiceImpl(SaleOrderRepository repo) { this.repo = repo; }

    public Long handle(CreateSaleCommand cmd) {
        boolean busy = repo.existsByVehicleIdAndStatusIn(
                cmd.vehicleId(), List.of(SaleStatus.RESERVED, SaleStatus.PAYMENT_PENDING, SaleStatus.PAID));
        if (busy) throw new BusinessRuleViolationException("Vehicle already has an active sale");


        var sale = new SaleOrder();
        sale.setVehicleId(cmd.vehicleId());
        sale.setBuyerId(cmd.buyerId());
        sale.setSellerId(cmd.sellerId());
        sale.setPriceAmount(cmd.priceAmount());
        sale.setPriceCurrency(cmd.priceCurrency());

        if (cmd.initialImages() != null && !cmd.initialImages().isEmpty())
            sale.addImages(cmd.initialImages(), MAX_IMAGES);

        sale.reserveUntil(Instant.now().plus(30, ChronoUnit.MINUTES));
        return repo.save(sale).getId();
    }

    public void handle(ConfirmPaymentCommand c){
        var sale = repo.findById(c.saleId()).orElseThrow(() -> new SaleNotFoundException(c.saleId()));
        sale.markPaid();
        repo.save(sale);
    }

    public void handle(CancelSaleCommand c){
        var sale = repo.findById(c.saleId()).orElseThrow(() -> new SaleNotFoundException(c.saleId()));
        sale.cancel();
        repo.save(sale);
    }



}
