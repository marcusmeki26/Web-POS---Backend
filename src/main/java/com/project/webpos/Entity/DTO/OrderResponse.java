package com.project.webpos.Entity.DTO;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderResponse {
  private Integer id;
  private String receiptNo;
  private Long foodId;
  private String shortFoodDesc;
  private BigDecimal price;
  private Integer sizeId;
  private String size;
  private Integer quantity;
  private Boolean placed;
  private Integer tableId;
  private Integer orderType;
}
