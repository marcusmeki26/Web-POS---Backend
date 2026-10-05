package com.project.webpos.Entity.DTO;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class FoodPatchRequest {
  // Name, Price, Category, Variants
  @NotBlank(message = "name is required")
  private String name;
  @NotBlank(message = "short description is required")
  private String shortDesc;
  @NotNull(message = "price is required")
  private BigDecimal price;
  @NotNull(message = "quantity is required")
  private Integer quantity;
  @NotBlank(message = "category is required")  
  private String category;  
  @NotNull(message = "with variant is required")
  private Boolean withVariant;
  private List<ProductVariantPatchRequest> variants;
}
