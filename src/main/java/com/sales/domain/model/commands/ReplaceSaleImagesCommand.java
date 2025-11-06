package com.sales.domain.model.commands;
import java.util.List;

public record ReplaceSaleImagesCommand(Long saleId, List<String> urls) {}
