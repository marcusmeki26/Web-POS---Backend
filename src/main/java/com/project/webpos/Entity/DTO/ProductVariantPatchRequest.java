package com.project.webpos.Entity.DTO;

import java.math.BigDecimal;

import org.openapitools.jackson.nullable.JsonNullable;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class ProductVariantPatchRequest {
  @NotNull(message = "size id is required")
  private Integer sizeId;
  // @NotNull (message = "size price is required")
  private JsonNullable<BigDecimal> sizePrice;
  // @NotNull (message = "size qantity is required")
  private JsonNullable<Integer> sizeQuantity;
  @NotNull (message = "old size id is required")
  private Integer sizeOldId;
}
