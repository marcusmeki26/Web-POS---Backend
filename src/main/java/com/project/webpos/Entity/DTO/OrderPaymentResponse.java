package com.project.webpos.Entity.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
@AllArgsConstructor 
public class OrderPaymentResponse {
  private Boolean processTransaction;
  private List<OrderResponse> orderList;
}
