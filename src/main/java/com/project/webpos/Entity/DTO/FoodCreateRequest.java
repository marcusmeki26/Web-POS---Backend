package com.project.webpos.Entity.DTO;

import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FoodCreateRequest {
  private String foodName;
  private String shortFoodDesc;
  private BigDecimal price;
  private Integer quantity;
  private MultipartFile imagePath;
  private String categoryType;
}
