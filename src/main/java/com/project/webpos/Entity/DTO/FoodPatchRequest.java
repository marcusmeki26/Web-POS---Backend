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
  @NotBlank(message = "name should not be empty or null")
  private String name;
  @NotBlank(message = "short description should not be empty or null")
  private String shortDesc;
  @NotNull(message = "price cannot be null")
  private BigDecimal price;
  @NotNull(message = "quantity cannot be null")
  private Integer quantity;
  @NotBlank(message = "category should not be empty")  
  private String category;  
  @NotNull(message = "with variant cannot be null")
  private Boolean withVariant;
  private List<ProductVariantPatchRequest> variants;
}
