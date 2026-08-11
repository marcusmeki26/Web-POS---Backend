package com.project.webpos.Entity.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryResponse {
  private Integer quantity;
  private ProductVariantResponse productVariant;
}
