package com.project.webpos.Entity.DTO;

import java.math.BigDecimal;

import com.project.webpos.Enum.EntryType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCreateRequest {
  private Integer id;
  @NotEmpty(message = "receipt no is required")
  private String receiptNo;
  @NotNull(message = "food id is required")
  private Long number;
  private String shortFoodDesc;
  private BigDecimal price;
  private Float discountPercent;
  private Integer sizeId;
  private String size;
  @NotNull(message = "entry type is required")
  private EntryType entryType;
  @NotNull(message = "quantity is required")
  @Min(value = 1, message = "quantity should be greater than 0")
  private Integer quantity;
  @NotNull(message = "placed is required")
  private Boolean placed;
  private Integer tableId;
}
