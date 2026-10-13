package com.project.webpos.Controller.v1;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
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
import com.project.webpos.Entity.DTO.OrderPatchRequest;
import com.project.webpos.Entity.DTO.OrderResponse;
import com.project.webpos.Entity.DTO.OrderPaymentCreateRequest;
import com.project.webpos.Entity.DTO.OrderPaymentResponse;
import com.project.webpos.Service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/order")
public class OrderController {
  private final OrderService orderService;

  @GetMapping
  public ResponseEntity<List<OrderResponse>> getOrders(
    @RequestParam(name = "table", required = false) Integer tableId
  ){
    if(tableId != null)
      return ResponseEntity.ok(orderService.getOrderByTableId(tableId));
    
    return ResponseEntity.ok(orderService.getOrders());
  }

  @GetMapping("/{foodId}")
  public ResponseEntity<List<OrderResponse>> getOrderByFoodId(@PathVariable Long foodId){
    return ResponseEntity.ok(orderService.getOrderByFoodId(foodId));
  }

  @GetMapping("/sum")
  public ResponseEntity<BigDecimal> getOrderSumUnplaced(){
    return ResponseEntity.ok(orderService.getOrderSumUnplaced());
  }

  @GetMapping("/current/receipt")
  public ResponseEntity<String> getCurrentReceiptNo(){
    return ResponseEntity.ok(orderService.getCurrentReceiptNo());
  }

  @GetMapping("/unplaced")
  public ResponseEntity<List<OrderResponse>> getUnplacedOrders(){
    return ResponseEntity.ok(orderService.getUnplacedOrders());
  }

  @PatchMapping("/{id}")
  public ResponseEntity<OrderList> patchOrder(@PathVariable Integer id, @RequestBody OrderPatchRequest orderPatchRequest){
    // return orderService.patchOrder(id, orderPatchRequest) ?
    //   ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    // CHANGE OrderList -> OrderResponse :REMOVE-COMMENT
    return ResponseEntity.ok().body(orderService.patchOrder(id, orderPatchRequest));
  }

  @PatchMapping("/receipt/{receiptNo}")
  public ResponseEntity<Void> patchOrderByReceiptNo(@PathVariable String receiptNo, @RequestBody OrderPatchRequest orderPatchRequest){
    // return orderService.patchOrderByReceiptNo(receiptNo, orderPatchRequest) ?
    //   ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    orderService.patchOrderByReceiptNo(receiptNo, orderPatchRequest);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/receipt/existing/{receiptNo}")
  public ResponseEntity<Void> patchOrdersByReceiptNo(
    @PathVariable String receiptNo, 
    @RequestBody List<OrderCreateRequest> orderPatchRequests
  ){
    orderService.addOrderToAnExistingOrder(receiptNo, orderPatchRequests);
    return ResponseEntity.ok().build();
  }

  @PatchMapping("/retrieve/{receiptNo}")
  public ResponseEntity<Void> patchRetrieveOrderByReceiptNo(
    @PathVariable String receiptNo, 
    @RequestBody OrderPatchRequest orderPatchRequest
  ){
    // return orderService.patchRetrieveOrder(receiptNo, orderPatchRequest) ? 
    //   ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    orderService.patchRetrieveOrder(receiptNo, orderPatchRequest);
    return ResponseEntity.noContent().build();
  }

  @PostMapping
  public ResponseEntity<Void> setOrder(@RequestBody OrderCreateRequest request){
    orderService.setOrder(request);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PostMapping("/payment")
  public ResponseEntity<OrderPaymentResponse> setPayment(@RequestBody OrderPaymentCreateRequest request){
    // return orderService.setPayment(request) ?
    //   ResponseEntity.status(HttpStatus.CREATED).build() : ResponseEntity.badRequest().build();
      OrderPaymentResponse orderPaymentResponse = orderService.setPayment(request);
      if(orderPaymentResponse.getProcessTransaction()){
        // OrderPaymentResponse response = new OrderPaymentResponse();
        // response.setProcessTransaction(true);
        return ResponseEntity.ok(orderPaymentResponse); 
      }else{
        // OrderPaymentResponse response = new OrderPaymentResponse();
        // response.setProcessTransaction(false);
        return ResponseEntity.ok(orderPaymentResponse); 
      }
  }

  @DeleteMapping
  public ResponseEntity<Void> deleteOrder(@RequestBody OrderDeleteRequest request){
    return orderService.deleteOrder(request) ? 
      ResponseEntity.ok().build() : ResponseEntity.badRequest().build(); 
  }
}
