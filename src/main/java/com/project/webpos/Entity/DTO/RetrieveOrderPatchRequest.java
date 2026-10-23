package com.project.webpos.Entity.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class RetrieveOrderPatchRequest {
  private Integer tableNo;
  private Integer orderType;
  @NotNull(message = "placed is required")
  private Boolean placed;
}
