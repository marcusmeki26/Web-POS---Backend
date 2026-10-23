package com.project.webpos.Entity.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
public class OrderPatchRequestAssignTableAndOrderType {
  @NotNull(message = "table no is required")
  private Integer tableNo;
  @NotNull(message = "order type is required")
  private Integer orderType;
  @NotNull(message = "placed is required")
  private Boolean placed;
}
