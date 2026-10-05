package com.project.webpos.Entity.DTO;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class ProductVariantCreateRequest {
  @NotNull (message = "size id is reqiured")
  private Integer sizeId;
  @NotNull (message = "size price is reqiured")
  private BigDecimal sizePrice;
  @NotNull (message = "size quantity is reqiured")
  private Integer sizeQuantity;
}
