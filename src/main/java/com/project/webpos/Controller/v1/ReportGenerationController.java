package com.project.webpos.Controller.v1;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.project.webpos.Service.Shared.TransactionManagementService;

import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/api/v1/generate-report")
@PreAuthorize("hasRole('OWNER')")
@RequiredArgsConstructor 
public class ReportGenerationController {
  private final TransactionManagementService transactionManagementService;

  @GetMapping("/x-report")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasAnyRole('OWNER','MANAGER')")
  public void generateXReport(){
    
  }

  @GetMapping("/y-report")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasAnyRole('OWNER','MANAGER')")
  public void generateYReport(){
    
  }

  @GetMapping("/z-report")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasAnyRole('OWNER','MANAGER')")
  public void generateZReport(){
    
  }
}
