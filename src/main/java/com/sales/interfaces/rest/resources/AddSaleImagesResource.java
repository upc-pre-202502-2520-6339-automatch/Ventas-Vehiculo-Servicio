package com.sales.interfaces.rest.resources;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record AddSaleImagesResource(@NotEmpty List<String> urls) {}
