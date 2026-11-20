package com.project.webpos.Entity.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
public class TransactionPaymentResponse {
  private String receiptNo;
  private LocalDateTime transactionDate;
  private Long number;
  private BigDecimal amountTendered;
  private String cardNo;
  private Boolean changeLine;
  private Integer cashierId;
  private Integer tableId;
}
