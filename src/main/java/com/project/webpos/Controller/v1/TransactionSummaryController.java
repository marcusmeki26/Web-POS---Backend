package com.project.webpos.Controller.v1;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.project.webpos.Entity.DTO.PagedResponse;
import com.project.webpos.Entity.DTO.SearchRequest;
import com.project.webpos.Entity.DTO.TransactionSummaryResponse;
import com.project.webpos.Service.TransactionSummaryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController 
@RequiredArgsConstructor
@RequestMapping("/api/v1/transaction-summary")
@PreAuthorize("hasRole('OWNER')")
public class TransactionSummaryController {
  private final TransactionSummaryService transactionSummaryService;
  // if dynamic handling of query parameter is possible
  @GetMapping 
  @ResponseStatus(HttpStatus.OK)
  @Operation(
    summary = "Retrieves the record of transaction summary",
    description = "Retrieves and returns a paginated record"
  )
  @ApiResponse(responseCode = "200", description = "Successfully retrieved")
  public PagedResponse<TransactionSummaryResponse> getTransactionSummary(
    // TransactionSummaryFilter transactionSummaryFilter,
    @PageableDefault(page = 0, size = 20, sort = "id") Pageable pageable
  ){
    return transactionSummaryService.getTransactionSummary(pageable);
  }

  @GetMapping("/fields")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
    summary = "Retrieves the fields of Transaction Summary",
    description = "Retrieves the fields of Transaction Summary and converts it into humanized form"
  )
  @ApiResponse(responseCode = "200", description = "Successfully retrieved")
  public List<String> getEntityFields(){
    return transactionSummaryService.getEntityFields();
  }

  @PostMapping("/search")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
    summary = "Retrieves the record of Transaction Payment based on the user filter request",
    description = "Retrieves the record of Transaction Payment based on the user filter request"
  )
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
    @ApiResponse(responseCode = "422", description = "Invalid operator or filter value")
  })
  public PagedResponse<TransactionSummaryResponse> getTransactionSummarySearch(
    @RequestBody SearchRequest searchRequest,
    @PageableDefault(page = 0, size = 20, sort = "id") Pageable pageable
  ){
    return transactionSummaryService.getTransactionSummarySearch(searchRequest, pageable);
  }
}
