package com.project.webpos.Entity.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class TransactionSummaryResponse {
  private String receiptNo;
  private String transactionNo;
  private LocalDateTime transactionDate;
  private Integer cashierId;
  private BigDecimal grossSales;
  private BigDecimal netSales;
  private BigDecimal vatAmount;
  private BigDecimal discountAmount;
  private BigDecimal payment;
  private Integer quantity;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
