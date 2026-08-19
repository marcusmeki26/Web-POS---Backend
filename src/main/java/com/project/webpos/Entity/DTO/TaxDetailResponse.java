package com.project.webpos.Entity.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaxDetailResponse {
  private Double taxPercentage;
  private String taxDescription;
}
