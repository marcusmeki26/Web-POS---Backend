package com.project.webpos.Entity.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderPatchRequest {
  private Integer quantity;
  private Integer tableNo;
  private Boolean placed;
  private Integer orderType;
}
