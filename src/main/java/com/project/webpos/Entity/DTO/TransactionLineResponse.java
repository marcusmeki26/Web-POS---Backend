package com.project.webpos.Entity.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class TransactionLineResponse {
  private String receiptNo;
  private LocalDateTime transactionDate;
  private BigDecimal grossAmount;
  private BigDecimal netAmount;
  private BigDecimal vatAmount;
  private BigDecimal discountAmount;
  private Integer quantity;
  private Long number;
  private String foodDescription;
  private BigDecimal price;
  private Integer sizeId;
  private String size;
  private Integer cashierId;
  private Integer tableId;
}
