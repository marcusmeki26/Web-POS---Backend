package com.project.webpos.Entity.DTO;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class OrderPaymentCreateRequest {
  @NotNull(message = "tender type is required")
  private Long tenderType;
  @NotNull(message = "payment is required")
  @Min(value = 1, message = "payment should be greater than 0")
  private BigDecimal payment;
  @NotNull(message = "total amount is required")
  @Min(value = 1, message = "total amount is required")
  private BigDecimal totalAmount;
  @NotEmpty(message = "tender description is required")
  private String tenderDescription;
  @NotEmpty(message = "receipt no. is required")
  private String receiptNo; 
  @NotNull(message = "table no is required")
  private Integer tableNo;
}
