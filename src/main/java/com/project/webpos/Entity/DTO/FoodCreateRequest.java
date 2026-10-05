package com.project.webpos.Entity.DTO;

import java.math.BigDecimal;
import java.util.List;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FoodCreateRequest {
  @NotEmpty (message = "food name is required")
  private String foodName;
  @NotEmpty (message = "short description is required")
  private String shortFoodDesc;
  private BigDecimal price;
  private Integer quantity;
  @NotNull (message = "category should is required")
  private String category;
  private Boolean withVariant;
  private List<ProductVariantCreateRequest> variants;
}
