package com.project.webpos.Entity.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class StoreInformationResponse {
  private String companyName;
  private String companyAddress;
  private String companyContactNo;
  private String companyEmail;
  private double taxPercent;
}
