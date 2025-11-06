package com.sales.application.internal.commandservices;

import com.sales.config.SecurityConfig;
import com.sales.domain.exceptions.BusinessRuleViolationException;
import com.sales.domain.exceptions.SaleNotFoundException;
import com.sales.domain.model.aggregates.SaleOrder;
import com.sales.domain.model.commands.CancelSaleCommand;
import com.sales.domain.model.commands.ConfirmPaymentCommand;
import com.sales.domain.model.commands.CreateSaleCommand;
import com.sales.domain.model.valueobjects.SaleStatus;
import com.sales.infrastructure.clients.VehicleClient;
import com.sales.infrastructure.persistence.jpa.SaleOrderRepository;
import com.sales.interfaces.rest.resources.VehicleSummary;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
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
    private final VehicleClient vehicles;
    private final SecurityConfig.AuthUser authUser; // ya lo tienes

    public SaleCommandServiceImpl(SaleOrderRepository repo,
                                  VehicleClient vehicles,
                                  SecurityConfig.AuthUser authUser) {
        this.repo = repo;
        this.vehicles = vehicles;
        this.authUser = authUser;
    }

    public Long handle(CreateSaleCommand cmd) {
        // 0) buyerId desde el JWT
        Long buyerId = authUser.currentUserId();

        // 1) ¿Ya hay venta activa para ese vehículo?
        boolean busy = repo.existsByVehicleIdAndStatusIn(
                cmd.vehicleId(), List.of(SaleStatus.RESERVED, SaleStatus.PAYMENT_PENDING, SaleStatus.PAID));
        if (busy) throw new BusinessRuleViolationException("Vehicle already has an active sale");

        // 2) Consultar Vehicle-Service y validar estado
        VehicleSummary v;
        try {
            v = vehicles.getById(cmd.vehicleId());
        } catch (feign.FeignException.NotFound nf) {
            throw new BusinessRuleViolationException("Vehicle not found");
        }

        if (!"PUBLISHED".equalsIgnoreCase(v.status()))
            throw new BusinessRuleViolationException("Vehicle is not available for sale");

        if (v.sellerId() != null && v.sellerId().equals(buyerId))
            throw new BusinessRuleViolationException("Seller cannot be the buyer");

        // 3) Crear la venta con SNAPSHOT de datos fuente del vehículo
        var sale = new SaleOrder();
        sale.setVehicleId(v.id());
        sale.setBuyerId(buyerId);
        sale.setSellerId(v.sellerId());
        sale.setPriceAmount(v.priceAmount());
        sale.setPriceCurrency(v.priceCurrency());

        var imgs = new java.util.ArrayList<String>();
        if (v.mainImageUrl() != null && !v.mainImageUrl().isBlank()) imgs.add(v.mainImageUrl());
        if (cmd.initialImages() != null) imgs.addAll(cmd.initialImages());
        if (!imgs.isEmpty()) sale.addImages(imgs, MAX_IMAGES);

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

    @Retry(name = "vehicles")
    @CircuitBreaker(name = "vehicles", fallbackMethod = "vehicleFallback")
    public VehicleSummary getVehicle(Long id) { return vehicles.getById(id); }

    private VehicleSummary vehicleFallback(Long id, Throwable ex) {
        throw new BusinessRuleViolationException("Vehicle service unavailable");
    }


}



