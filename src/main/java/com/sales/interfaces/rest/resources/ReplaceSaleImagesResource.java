package com.sales.interfaces.rest.resources;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record ReplaceSaleImagesResource(@NotEmpty List<String> urls) {}
