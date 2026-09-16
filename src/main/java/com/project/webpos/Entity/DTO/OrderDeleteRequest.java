package com.project.webpos.Entity.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDeleteRequest {
  private String receiptNo;
  private Long number;
}
