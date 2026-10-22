package com.project.webpos.Entity.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class SizeCreateRequest {
  @NotBlank(message = "size name is required")
  private String sizeName;
}
