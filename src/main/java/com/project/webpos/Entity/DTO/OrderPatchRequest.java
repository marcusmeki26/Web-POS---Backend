package com.project.webpos.Entity.DTO;

import java.math.BigDecimal;

import com.project.webpos.Enum.EntryType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderPatchRequest {
  private Integer id;
  private Integer quantity;
  private Integer tableNo;
  private Boolean placed;
  private Integer orderType;
  private String receiptNo;
  private Long number;
  private Integer sizeId;
  private BigDecimal amount;
  private BigDecimal discountAmount;
  private EntryType entryType;
}
