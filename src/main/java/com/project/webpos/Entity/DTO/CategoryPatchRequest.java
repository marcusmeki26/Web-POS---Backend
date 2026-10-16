package com.project.webpos.Entity.DTO;

import org.openapitools.jackson.nullable.JsonNullable;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class CategoryPatchRequest {
  @NotNull(message = "id is requried")
  private Integer id;
  JsonNullable<String> code = JsonNullable.undefined();
}
