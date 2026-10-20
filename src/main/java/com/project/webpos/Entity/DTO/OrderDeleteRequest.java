package com.project.webpos.Entity.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDeleteRequest {
  @NotEmpty(message = "receipt no. should not be empty")
  private String receiptNo;
  @NotNull(message = "food id should not be empty")
  private Long number;
}
