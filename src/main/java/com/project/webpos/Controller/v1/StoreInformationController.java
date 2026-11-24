package com.project.webpos.Controller.v1;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.project.webpos.Entity.DTO.StoreInformationPostOrPutRequest;
import com.project.webpos.Entity.DTO.StoreInformationResponse;
import com.project.webpos.Record.CompanyInformationResponse;
import com.project.webpos.Record.TaxPercentResponse;
import com.project.webpos.Service.StoreInformationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequiredArgsConstructor 
@RequestMapping("/api/v1/store-information")
@PreAuthorize("hasRole('OWNER')")
public class StoreInformationController {
  private final StoreInformationService storeInformationService;
  
  @GetMapping 
  @ResponseStatus(HttpStatus.OK)
  public StoreInformationResponse getStoreInformation(){
    return storeInformationService.getStoreInformation();
  }

  @PreAuthorize("hasAnyRole('CASHIER', 'MANAGER', 'OWNER')")
  @GetMapping("/company-information")
  @ResponseStatus(HttpStatus.OK)
  public CompanyInformationResponse getCompanyInformation(){
    return storeInformationService.getCompanyInformation();
  }

  @PreAuthorize("hasAnyRole('CASHIER', 'MANAGER', 'OWNER')")
  @GetMapping("/tax-percent")
  @ResponseStatus(HttpStatus.OK)
  public TaxPercentResponse getTaxPercent(){
    return storeInformationService.getTaxPercent();
  }

  @PutMapping
  @ResponseStatus(HttpStatus.OK)
  public StoreInformationResponse updateStoreInformation(
    @Valid @RequestBody StoreInformationPostOrPutRequest storeInformationUpdateRequest
  ){
    return storeInformationService.updateStoreInformation(storeInformationUpdateRequest);
  }
}
