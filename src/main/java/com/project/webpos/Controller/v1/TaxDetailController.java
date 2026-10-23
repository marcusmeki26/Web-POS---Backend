package com.project.webpos.Controller.v1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.webpos.Entity.DTO.TaxDetailResponse;
import com.project.webpos.Service.TaxDetailService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/v1/tax")
public class TaxDetailController {
  private final TaxDetailService taxDetailService;

  @GetMapping("/{isActive}")
  public TaxDetailResponse getActiveTax(@PathVariable Boolean isActive){

    return taxDetailService.getActiveTax(isActive);
  }
}
