package com.sales.application.jobs;

import com.sales.domain.model.aggregates.SaleOrder;
import com.sales.domain.model.valueobjects.SaleStatus;
import com.sales.infrastructure.persistence.jpa.SaleOrderRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ReservationExpirationJob {
    private final SaleOrderRepository repo;
    public ReservationExpirationJob(SaleOrderRepository repo){ this.repo = repo; }

    // cada minuto (ajusta a gusto)
    @Scheduled(fixedDelay = 60_000)
    public void expire() {
        var now = Instant.now();
        var toExpire = repo.findAllByStatusAndReservationExpiresAtBefore(SaleStatus.RESERVED, now);
        toExpire.forEach(SaleOrder::expire);
        repo.saveAll(toExpire);
    }
}
