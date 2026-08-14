package com.project.webpos.Controller.v1;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.webpos.Entity.DTO.OrderCreateRequest;
import com.project.webpos.Entity.DTO.OrderPatchRequest;
import com.project.webpos.Entity.DTO.OrderResponse;
import com.project.webpos.Service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/order")
public class OrderController {
  private final OrderService orderService;

  @GetMapping
  public ResponseEntity<List<OrderResponse>> getOrders(){
    return ResponseEntity.ok(orderService.getOrders());
  }

  @GetMapping("/{foodId}")
  public ResponseEntity<OrderResponse> getOrder(@PathVariable Long foodId){
    return ResponseEntity.ok(orderService.getOrder(foodId));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Void> patchOrder(@PathVariable Integer id, @RequestBody OrderPatchRequest orderPatchRequest){
    return orderService.patchOrder(id, orderPatchRequest) ?
      ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
  }

  @PostMapping
  public ResponseEntity<Void> setOrder(@RequestBody OrderCreateRequest request){
    return orderService.setOrder(request) ? 
      ResponseEntity.status(HttpStatus.CREATED).build() : ResponseEntity.badRequest().build();
  }
}
