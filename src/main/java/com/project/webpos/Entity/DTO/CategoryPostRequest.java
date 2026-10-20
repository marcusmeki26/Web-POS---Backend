package com.project.webpos.Entity.DTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class CategoryPostRequest {
  @NotEmpty(message = "category name is required")
  private String code;
}
