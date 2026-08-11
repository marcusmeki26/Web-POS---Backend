package com.project.webpos.Entity.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CategoryCountResponse {
  private String categoryType;
  private Long categoryCount;
}
