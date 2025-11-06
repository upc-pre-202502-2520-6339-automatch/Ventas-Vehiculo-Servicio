package com.sales.infrastructure.persistence.jpa;

import com.sales.domain.model.aggregates.SaleOrder;
import com.sales.domain.model.valueobjects.SaleStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

public interface SaleOrderRepository extends JpaRepository<SaleOrder, Long> {
    boolean existsByVehicleIdAndStatusIn(Long vehicleId, Collection<SaleStatus> statuses);

    List<SaleOrder> findAllByStatusAndReservationExpiresAtBefore(
            SaleStatus status, Instant before);








}
