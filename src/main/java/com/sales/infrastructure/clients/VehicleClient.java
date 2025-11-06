package com.sales.infrastructure.clients;

import com.sales.interfaces.rest.resources.VehicleSummary;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "vehicles", path = "/api/v1/vehicles")
public interface VehicleClient {
    @GetMapping("/{id}")
    VehicleSummary getById(@PathVariable Long id);
}
