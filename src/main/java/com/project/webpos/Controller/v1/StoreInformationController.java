package com.project.webpos.Controller.v1;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.project.webpos.Entity.DTO.StoreInformationPutRequest;
import com.project.webpos.Entity.DTO.StoreInformationResponse;
import com.project.webpos.Record.CompanyInformationResponse;
import com.project.webpos.Record.TaxPercentResponse;
import com.project.webpos.Service.StoreInformationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
  @Operation(
    summary = "Retrieves all store infromation",
    description = "Retrieves all store information from store information table. If there is no record found it will return a default values of each field"
  )
  @ApiResponse(responseCode = "200", description = "Successfully retrieved")
  public StoreInformationResponse getStoreInformation(){
    return storeInformationService.getStoreInformation();
  }

  @PreAuthorize("hasAnyRole('CASHIER', 'MANAGER', 'OWNER')")
  @GetMapping("/company-information")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
    summary = "Retrieves only the company information",
    description = "Retrieves only the company information from store information table. If there is no record found it will return a default values of each field"
  )
  @ApiResponse(responseCode = "200", description = "Successfully retrieved")
  public CompanyInformationResponse getCompanyInformation(){
    return storeInformationService.getCompanyInformation();
  }

  @PreAuthorize("hasAnyRole('CASHIER', 'MANAGER', 'OWNER')")
  @GetMapping("/tax-percent")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
    summary = "Retrieves only the tax percent",
    description = "Retrieves only the tax percent set on store information table. If there is no record found it will return a default values of each field"
  )
  @ApiResponse(responseCode = "200", description = "Successfully retrieved")
  public TaxPercentResponse getTaxPercent(){
    return storeInformationService.getTaxPercent();
  }

  @PutMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation(
    summary = "Updates the record based on the user payload request",
    description = "Performs an update on the store information based on the user payload request. If there is no existing record it will return a default values of each field"
  )
  @ApiResponse(responseCode = "200", description = "Successfully updated")
  public StoreInformationResponse updateStoreInformation(
    @Valid @RequestBody StoreInformationPutRequest storeInformationUpdateRequest
  ){
    return storeInformationService.updateStoreInformation(storeInformationUpdateRequest);
  }
}
