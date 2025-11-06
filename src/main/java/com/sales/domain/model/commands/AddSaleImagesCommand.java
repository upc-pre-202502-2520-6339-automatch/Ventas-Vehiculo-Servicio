package com.sales.domain.model.commands;
import java.util.List;

public record AddSaleImagesCommand(Long saleId, List<String> urls) {}
