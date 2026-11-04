package com.project.webpos.Controller.v1;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.project.webpos.Entity.DTO.TaxDetailResponse;
import com.project.webpos.Service.TaxDetailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/tax")
public class TaxDetailController {
  private final TaxDetailService taxDetailService;

  @GetMapping("/active")
  @ResponseStatus(HttpStatus.OK)
  public TaxDetailResponse getActiveTax(){
    return taxDetailService.getActiveTax();
  }
}
