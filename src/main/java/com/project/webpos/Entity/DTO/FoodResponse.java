package com.project.webpos.Entity.DTO;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FoodResponse {
  private Long id;
  private String foodName;
  private String shortFoodDesc;
  private BigDecimal price;
  private Boolean withVariant;
  private String imageName;
  private String categoryType;
  private List<InventoryResponse> inventory;
  private Integer quantity;
}
