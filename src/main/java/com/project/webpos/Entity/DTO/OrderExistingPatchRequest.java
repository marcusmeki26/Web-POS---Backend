package com.project.webpos.Entity.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderExistingPatchRequest {
  @NotNull(message = "order id is required")
  private Integer id;
  @NotNull(message = "food is is required")
  private Long number;
  @NotNull(message = "quantity is required")
  @Min(value = 1, message = "quantity should be greater than zero")
  private Integer quantity;
  private Integer sizeId;
  @NotNull(message = "table id is required")
  private Integer tableId;
}
