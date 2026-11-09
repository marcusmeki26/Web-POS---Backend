package com.project.webpos.Controller.v1;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.project.webpos.Entity.DTO.InventorySummaryResponse;
import com.project.webpos.Entity.DTO.PagedResponse;
import com.project.webpos.Service.InventoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController 
@RequiredArgsConstructor 
@RequestMapping("/api/v1/inventory")
public class InventoryController {

  private final InventoryService inventoryService;

  @GetMapping 
  @ResponseStatus(HttpStatus.OK)
  @Operation(
    summary = "Retrieves record of Inventory Summary",
    description = """
      Retrieves the record of Inventory Summary which fetches related record from other table and mapped into a flat DTO.
      Inventory * Food.
      Inventory & ProductVariant
      ProductVariant & Size
    """
  )
  @ApiResponse(responseCode = "200", description = "Successfully retrieved records")
  public PagedResponse<List<InventorySummaryResponse>> getInventorySummary(
    @PageableDefault(page = 0, size = 5, sort = "id") Pageable pageable
  ){
    return inventoryService.getInventorySummary(pageable);
  }
}
