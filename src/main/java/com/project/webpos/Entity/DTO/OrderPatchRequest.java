package com.project.webpos.Entity.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderPatchRequest {
  private Integer id;
  private Integer quantity;
  private Integer tableNo;
  private Boolean placed;
  private Integer orderType;
  private String receiptNo;
  private Long foodId;
  private Integer sizeId;
}
