package com.project.webpos.Entity.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class TransactionSummaryFilter {
  private Integer cashierId;
  private LocalDateTime createdAt;
  private BigDecimal discountAmount;
  private BigDecimal grossSales;
  private BigDecimal netSales;
  private BigDecimal payment;
  private Integer quantity;
  private String receiptNo;
  private LocalDateTime transactionDate;
  private String transactionNo;
  private LocalDateTime updatedAt;
  private BigDecimal vatAmount;
}
