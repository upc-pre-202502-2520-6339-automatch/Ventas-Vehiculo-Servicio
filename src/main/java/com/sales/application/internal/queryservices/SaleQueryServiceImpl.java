package com.sales.application.internal.queryservices;

import com.sales.domain.exceptions.SaleNotFoundException;
import com.sales.domain.model.aggregates.SaleOrder;
import com.sales.domain.model.queries.GetSaleByIdQuery;
import com.sales.infrastructure.persistence.jpa.SaleOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SaleQueryServiceImpl {
    private final SaleOrderRepository repo;
    public SaleQueryServiceImpl(SaleOrderRepository repo){ this.repo = repo; }

    public SaleOrder handle(GetSaleByIdQuery q){
        return repo.findById(q.saleId())
                .orElseThrow(() -> new SaleNotFoundException(q.saleId()));
    }



}
