// com.sales.interfaces.rest.SalesController
package com.sales.interfaces.rest;

import com.sales.application.internal.commandservices.SaleCommandServiceImpl;
import com.sales.application.internal.queryservices.SaleQueryServiceImpl;
import com.sales.domain.model.queries.GetSaleByIdQuery;
import com.sales.interfaces.rest.resources.CancelSaleResource;
import com.sales.interfaces.rest.resources.CreateSaleResource;
import com.sales.interfaces.rest.resources.SaleResource;
import com.sales.interfaces.rest.transformers.CancelSaleCommandFromResourceAssembler;
import com.sales.interfaces.rest.transformers.ConfirmPaymentCommandFromResourceAssembler;
import com.sales.interfaces.rest.transformers.CreateSaleCommandFromResourceAssembler;
import com.sales.interfaces.rest.transformers.SaleResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sales")
public class SalesController {

    private final SaleCommandServiceImpl commandService;
    private final SaleQueryServiceImpl queryService;

    public SalesController(SaleCommandServiceImpl commandService, SaleQueryServiceImpl queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @Operation(summary="Create sale (reserve vehicle)")
    @ApiResponses({
            @ApiResponse(responseCode="200", description="Sale created"),
            @ApiResponse(responseCode="400", description="Invalid data or active sale exists")
    })
    @PostMapping
    public ResponseEntity<SaleResource> create(@RequestBody @Valid CreateSaleResource body){
        var id = commandService.handle(CreateSaleCommandFromResourceAssembler.toCommandFromResource(body));
        var sale = queryService.handle(new GetSaleByIdQuery(id));
        return ResponseEntity.ok(SaleResourceFromEntityAssembler.toResourceFromEntity(sale));
    }

    @Operation(summary="Get sale by ID")
    @ApiResponses({
            @ApiResponse(responseCode="200", description="Sale returned"),
            @ApiResponse(responseCode="404", description="Sale not found")
    })
    @GetMapping("{id}")
    public ResponseEntity<SaleResource> get(@PathVariable Long id){
        var sale = queryService.handle(new GetSaleByIdQuery(id));
        return ResponseEntity.ok(SaleResourceFromEntityAssembler.toResourceFromEntity(sale));
    }

    @Operation(summary="Confirm sale payment")
    @ApiResponses({@ApiResponse(responseCode="204", description="Sale marked as PAID")})
    @PostMapping("{id}/pay")
    public ResponseEntity<Void> confirmPayment(@PathVariable Long id){
        commandService.handle(ConfirmPaymentCommandFromResourceAssembler.toCommand(id));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary="Cancel sale")
    @ApiResponses({@ApiResponse(responseCode="204", description="Sale cancelled")})
    @PostMapping("{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable Long id, @RequestBody(required=false) CancelSaleResource body){
        commandService.handle(CancelSaleCommandFromResourceAssembler.toCommand(id, body));
        return ResponseEntity.noContent().build();
    }



}
