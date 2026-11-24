package com.project.webpos.Entity.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class StoreInformationPutRequest {
  @NotBlank(message = "company name is required")
  private String companyName;
  @NotBlank(message = "company address is required")
  private String companyAddress;
  @NotBlank(message = "contact number is requred")
  @Size(min = 11, max = 11, message = "contact number should be exactly 11 numbers")
  @Pattern(
    regexp = "^\\d{11}$",
    message = "invalid contact number format"
  )
  private String companyContactNo;
  @NotBlank(message = "company email is required")
  private String companyEmail;
  private Double taxPercent;
}
