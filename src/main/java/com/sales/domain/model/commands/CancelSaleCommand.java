package com.sales.domain.model.commands;

public record CancelSaleCommand(Long saleId, String reason) {}