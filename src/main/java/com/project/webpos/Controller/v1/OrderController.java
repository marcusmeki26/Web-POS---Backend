package com.project.webpos.Controller.v1;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.webpos.Entity.OrderList;
import com.project.webpos.Entity.DTO.OrderCreateRequest;
import com.project.webpos.Entity.DTO.OrderDeleteRequest;
import com.project.webpos.Entity.DTO.OrderExistingPatchRequest;
import com.project.webpos.Entity.DTO.OrderPatchRequest;
import com.project.webpos.Entity.DTO.OrderResponse;
import com.project.webpos.Exception.ErrorMessage;
import com.project.webpos.Entity.DTO.OrderPaymentCreateRequest;
import com.project.webpos.Entity.DTO.OrderPaymentResponse;
import com.project.webpos.Service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/order")
@Tag(name = "Order", description = "Endpoint for managing order")
public class OrderController {
  private final OrderService orderService;

  @GetMapping
  @Operation(
    summary = "Retrieves all order or retrieves based on table ID",
    description = "Fetches all order or fetches all order based on table ID if it is not null"
  )
  @ApiResponse(responseCode = "200", description = "Successfully retrieved order/s")
  public ResponseEntity<List<OrderResponse>> getOrders(
    @Parameter(description = "ID of the table to search", required = false, example = "1")
    @RequestParam(name = "table", required = false) Integer tableId
  ){
    if(tableId != null)
      return ResponseEntity.ok(orderService.getOrderByTableId(tableId));
    
    return ResponseEntity.ok(orderService.getOrders());
  }

  @GetMapping("/{foodId}")
  @Operation(
    summary = "Retrieves order based on its food ID",
    description = "Fetches order based on the specified food ID and unplaced"
  ) 
  @ApiResponse(responseCode = "200", description = "Successfully retrieved order/s")
  public ResponseEntity<List<OrderResponse>> getOrderByFoodId(
    @Parameter(description = "ID of the food to search", required = true, example = "10000")
    @PathVariable Long foodId
  ){
    return ResponseEntity.ok(orderService.getOrderByFoodId(foodId));
  }

  @GetMapping("/sum")
  @Operation(
    summary = "Gets the sum of the order",
    description = "Retrieves the sum of unplaced orders"
  )
  @ApiResponse(responseCode = "200", description = "Successfully retrieved the sum of unplaced orders")
  public ResponseEntity<BigDecimal> getOrderSumUnplaced(){
    return ResponseEntity.ok(orderService.getOrderSumUnplaced());
  }

  @GetMapping("/current/receipt")
  @Operation(
    summary = "Generates the current receipt no.",
    description = "Generates the current receipt no. If the table is empty start with R000001"
  )
  @ApiResponse(responseCode = "200", description = "Successfully retrieved current receipt no.")
  public ResponseEntity<String> getCurrentReceiptNo(){
    return ResponseEntity.ok(orderService.getCurrentReceiptNo());
  }

  @GetMapping("/unplaced")
  @Operation(
    summary = "Retrieves all unplaced orders",
    description = "Fetches all unplaced orders"
  )
  @ApiResponse(responseCode = "200", description = "Successfully retrieved orders")
  public ResponseEntity<List<OrderResponse>> getUnplacedOrders(){
    return ResponseEntity.ok(orderService.getUnplacedOrders());
  }

  @PatchMapping("/{id}")
  @Operation(
    summary = "Partially modify an order based on order ID",
    description = "Updates the quantity of an order"
  )
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Successfully updated"),
    @ApiResponse(responseCode = "404", description = "Order not found")
  })
  public ResponseEntity<OrderList> patchOrder(
    @Parameter(description = "Target ID of order to be updated", required = true)
    @PathVariable Integer id, 
    @RequestBody OrderPatchRequest orderPatchRequest
  ){
    return ResponseEntity.ok().body(orderService.patchOrder(id, orderPatchRequest));
  }

  @PatchMapping("/receipt/{receiptNo}")
  @Operation(
    summary = "Partially modify an order based on receipt no.",
    description = "Assigning of table and order type"
  )
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Successfully updated"),
    @ApiResponse(responseCode = "422", description = "Unable to process: failed on business logic", content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
    @ApiResponse(responseCode = "404", description = "No existing record", content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
  })
  public ResponseEntity<Void> patchOrderByReceiptNo(
    @Parameter(description = "Target receipt no. of order to be updated", required = true)
    @PathVariable String receiptNo, 
    @RequestBody OrderPatchRequest orderPatchRequest
  ){
    orderService.patchOrderByReceiptNo(receiptNo, orderPatchRequest);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/receipt/existing/{receiptNo}")
  @Operation(
    summary = "Partially modify an order based on receipt no.",
    description = "Moved an unplaced order into a table that has existing order. If the same food is exsiting add the quantity. Else modify the table ID"
  )
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Successfully added"),
    @ApiResponse(responseCode = "400", description = "Validation failed on the payload fields", content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
  })
  public ResponseEntity<Void> patchOrdersByReceiptNo(
    @Parameter(description = "Target receipt no. of order to be updated", required = true)
    @PathVariable String receiptNo, 
    @Valid @RequestBody List<OrderExistingPatchRequest> orderPatchRequest
  ){
    orderService.addOrderToAnExistingOrder(receiptNo, orderPatchRequest);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/retrieve/{receiptNo}")
  @Operation(
    summary = "Partially modify ab order based on receipt no.",
    description = "Modifies 'placed' field into false. Used during retrieving of order from table to POSs"
  )
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Successfully updated"),
    @ApiResponse(responseCode = "404", description = "No existing record", content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
  })
  public ResponseEntity<Void> patchRetrieveOrderByReceiptNo(
    @Parameter(description = "Target receipt no. of order to be updated", required = true)
    @PathVariable String receiptNo, 
    @RequestBody OrderPatchRequest orderPatchRequest
  ){
    orderService.patchRetrieveOrder(receiptNo, orderPatchRequest);
    return ResponseEntity.noContent().build();
  }

  @PostMapping
  @Operation(
    summary = "Create a new order",
    description = "Adds new order"
  )
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Successfully created"),
    @ApiResponse(responseCode = "404", description = "No existing record", content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
    @ApiResponse(responseCode = "400", description = "Validation failed on the payload fields", content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
  })
  public ResponseEntity<Void> setOrder(
    @Valid @RequestBody OrderCreateRequest request
  ){
    orderService.setOrder(request);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/payment")
  @Operation(
    summary = "Handles processing of payment",
    description = "Performs processing of payment. If payment is not enough, subtract it with the balance. Else move the order into transaction summary, payment, and line"
  )
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Payment successful"),
    @ApiResponse(responseCode = "422", description = "Unable to process: failed during business logic", content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
    @ApiResponse(responseCode = "404", description = "No existing record", content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
    @ApiResponse(responseCode = "400", description = "Validation failed on the payload fields", content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
  })
  public ResponseEntity<OrderPaymentResponse> setPayment(
    @Valid @RequestBody OrderPaymentCreateRequest request
  ){
      OrderPaymentResponse orderPaymentResponse = orderService.setPayment(request);
      if(orderPaymentResponse.getProcessTransaction()){
        return ResponseEntity.ok(orderPaymentResponse); 
      }else{
        return ResponseEntity.ok(orderPaymentResponse); 
      }
  }

  @DeleteMapping
  @Operation(
    summary = "Deletes an order",
    description = "Hard deletes an order based on the food id & receipt no."
  )
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Successfully deleted"),
    @ApiResponse(responseCode = "422", description = "Unable to process: failed during business logic", content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
    @ApiResponse(responseCode = "400", description = "Validation failed on the payload fields", content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
  })
  public ResponseEntity<Void> deleteOrder(
    @Valid @RequestBody OrderDeleteRequest request
  ){
    orderService.deleteOrder(request); 
    return ResponseEntity.ok().build();
  }
}
