package com.project.webpos.Entity.DTO;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductVariantResponse {
  private SizeResponse size;
  private BigDecimal price;
}
