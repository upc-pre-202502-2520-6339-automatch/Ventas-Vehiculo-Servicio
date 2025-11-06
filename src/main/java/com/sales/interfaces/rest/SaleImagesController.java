package com.sales.interfaces.rest;

import com.sales.application.internal.commandservices.SaleImagesCommandServiceImpl;
import com.sales.interfaces.rest.resources.AddSaleImagesResource;
import com.sales.interfaces.rest.resources.ReplaceSaleImagesResource;
import com.sales.interfaces.rest.transformers.AddSaleImagesCommandFromResourceAssembler;
import com.sales.interfaces.rest.transformers.RemoveSaleImageAtCommandFromResourceAssembler;
import com.sales.interfaces.rest.transformers.ReplaceSaleImagesCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sales/{saleId}/images")
public class SaleImagesController {

    private final SaleImagesCommandServiceImpl imagesService;

    public SaleImagesController(SaleImagesCommandServiceImpl imagesService) {
        this.imagesService = imagesService;
    }

    @Operation(summary="Append images to sale")
    @ApiResponses({
            @ApiResponse(responseCode="204", description="Images appended"),
            @ApiResponse(responseCode="400", description="Invalid data")
    })
    @PostMapping
    public ResponseEntity<Void> add(@PathVariable Long saleId, @RequestBody @Valid AddSaleImagesResource body){
        imagesService.handle(AddSaleImagesCommandFromResourceAssembler.toCommandFromResource(saleId, body));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary="Replace all images of sale")
    @ApiResponses({
            @ApiResponse(responseCode="204", description="Images replaced"),
            @ApiResponse(responseCode="400", description="Invalid data")
    })
    @PutMapping
    public ResponseEntity<Void> replace(@PathVariable Long saleId, @RequestBody @Valid ReplaceSaleImagesResource body){
        imagesService.handle(ReplaceSaleImagesCommandFromResourceAssembler.toCommandFromResource(saleId, body));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary="Remove image at position")
    @ApiResponses({
            @ApiResponse(responseCode="204", description="Image removed"),
            @ApiResponse(responseCode="400", description="Invalid position")
    })
    @DeleteMapping("/{position}")
    public ResponseEntity<Void> removeAt(@PathVariable Long saleId, @PathVariable int position){
        imagesService.handle(RemoveSaleImageAtCommandFromResourceAssembler.at(saleId, position));
        return ResponseEntity.noContent().build();
    }
}
