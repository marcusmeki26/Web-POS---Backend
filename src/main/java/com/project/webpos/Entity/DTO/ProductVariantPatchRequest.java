package com.project.webpos.Entity.DTO;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class ProductVariantPatchRequest {
  @NotNull(message = "size id should not be null")
  private Integer sizeId;
  @NotNull (message = "size price should not be empty or null")
  private BigDecimal sizePrice;
  @NotNull (message = "size qantity should not be empty or null")
  private Integer sizeQuantity;
  @NotNull (message = "old size id shout not be null")
  private Integer sizeOldId;
}
