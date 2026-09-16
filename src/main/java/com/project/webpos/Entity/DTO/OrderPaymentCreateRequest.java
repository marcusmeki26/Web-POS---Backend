package com.project.webpos.Entity.DTO;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class OrderPaymentCreateRequest {
  private Long number;
  private BigDecimal payment;
  private BigDecimal totalAmount;
  private String shortFoodDesc;
  private String receiptNo; 
  private Integer tableNo;
}
