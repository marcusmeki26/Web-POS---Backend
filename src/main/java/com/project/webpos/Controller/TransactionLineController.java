package com.project.webpos.Controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.project.webpos.Entity.DTO.PagedResponse;
import com.project.webpos.Entity.DTO.SearchRequest;
import com.project.webpos.Entity.DTO.TransactionLineResponse;
import com.project.webpos.Service.TransactionLineService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController 
@RequiredArgsConstructor 
@RequestMapping("/api/v1/transaction-line")
public class TransactionLineController {
  private final TransactionLineService transactionLineService;

  @GetMapping 
  @ResponseStatus(HttpStatus.OK)
  @Operation(
    summary = "Retrieves a record of transaction line",
    description = "Retrives a record of transaction line and returns a paginated record"
  )
  @ApiResponse(responseCode = "200", description = "Successfully retrieved")
  public PagedResponse<TransactionLineResponse> getTransactionLine(
    @PageableDefault(page = 0, size = 20, sort = "receiptNo") Pageable pageable
  ){
    return transactionLineService.getTransactionLine(pageable);
  }

  @GetMapping("/fields")
  @ResponseStatus(HttpStatus.OK)
  public List<String> getEntityFields(){
    return transactionLineService.getEntityFields();
  }

  @PostMapping("/search")
  @ResponseStatus(HttpStatus.OK)
  public PagedResponse<TransactionLineResponse> getTransactionLineSearch(
    @RequestBody SearchRequest searchRequest,
    @PageableDefault(page = 0, size = 20, sort = "receiptNo") Pageable pageable
  ){
    return transactionLineService.getTransactionLineSearch(searchRequest, pageable);
  }
}
