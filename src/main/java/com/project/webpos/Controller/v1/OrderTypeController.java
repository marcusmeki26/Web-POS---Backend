package com.project.webpos.Controller.v1;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.webpos.Entity.DTO.OrderTypeResponse;
import com.project.webpos.Service.OrderTypeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/order-type")
@Tag(name = "Order Type", description = "Endpoints for managing order type")
public class OrderTypeController {
  private final OrderTypeService orderTypeService;

  @GetMapping
  @Operation(
    summary = "Retrieves all order type",
    description = "Fetches all order type"
  )
  @ApiResponse(responseCode = "200", description = "Sucessfully retrieved")
  public ResponseEntity<List<OrderTypeResponse>> getOrderTypes(){
    return ResponseEntity.ok(orderTypeService.getOrderTypes());
  }
}
