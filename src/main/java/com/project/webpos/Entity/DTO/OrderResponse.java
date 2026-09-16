package com.project.webpos.Entity.DTO;

import java.math.BigDecimal;

import com.project.webpos.Enum.EntryType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderResponse {
  private Integer id;
  private String receiptNo;
  private Long number;
  private String shortFoodDesc;
  private BigDecimal price;
  private BigDecimal amount;
  private BigDecimal discountAmount;
  private Integer sizeId;
  private String size;
  private EntryType entryType;
  private Integer quantity;
  private Boolean placed;
  private Integer tableId;
  private Integer orderType;
}
