package com.project.webpos.Entity.DTO;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class ProductVariantPatchRequest {
  @NotNull(message = "size id is required")
  private Integer sizeId;
  @NotNull (message = "size price is required")
  private BigDecimal sizePrice;
  @NotNull (message = "size qantity is required")
  private Integer sizeQuantity;
  @NotNull (message = "old size id is required")
  private Integer sizeOldId;
}
