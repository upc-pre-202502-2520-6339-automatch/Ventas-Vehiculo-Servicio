package com.sales.application.internal.commandservices;

import com.sales.domain.exceptions.SaleNotFoundException;
import com.sales.domain.model.commands.AddSaleImagesCommand;
import com.sales.domain.model.commands.RemoveSaleImageAtCommand;
import com.sales.domain.model.commands.ReplaceSaleImagesCommand;
import com.sales.infrastructure.persistence.jpa.SaleOrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class SaleImagesCommandServiceImpl {
    private static final int MAX_IMAGES = 10;
    private final SaleOrderRepository repo;

    public SaleImagesCommandServiceImpl(SaleOrderRepository repo) { this.repo = repo; }

    public void handle(AddSaleImagesCommand c){
        var sale = repo.findById(c.saleId()).orElseThrow(() -> new SaleNotFoundException(c.saleId()));
        sale.addImages(c.urls(), MAX_IMAGES);
        repo.save(sale);
    }
    public void handle(ReplaceSaleImagesCommand c){
        var sale = repo.findById(c.saleId()).orElseThrow(() -> new SaleNotFoundException(c.saleId()));
        sale.replaceImages(c.urls(), MAX_IMAGES);
        repo.save(sale);
    }
    public void handle(RemoveSaleImageAtCommand c){
        var sale = repo.findById(c.saleId()).orElseThrow(() -> new SaleNotFoundException(c.saleId()));
        sale.removeImageAt(c.position());
        repo.save(sale);
    }
}
