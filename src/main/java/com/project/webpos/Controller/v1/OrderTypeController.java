package com.project.webpos.Controller.v1;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.webpos.Entity.DTO.OrderTypeResponse;
import com.project.webpos.Service.OrderTypeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/order-type")
public class OrderTypeController {
  private final OrderTypeService orderTypeService;

  @GetMapping
  public ResponseEntity<List<OrderTypeResponse>> getOrderTypes(){
    return ResponseEntity.ok(orderTypeService.getOrderTypes());
  }
}
